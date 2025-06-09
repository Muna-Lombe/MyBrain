package com.mhss.app.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhss.app.domain.AiConstants
import com.mhss.app.domain.use_case.ExtendTextUseCase
import com.mhss.app.domain.use_case.GenerateCanvasImageUseCase
import com.mhss.app.domain.use_case.MakeConciseUseCase
import com.mhss.app.domain.use_case.SummarizeTextUseCase
import com.mhss.app.network.NetworkResult
import com.mhss.app.preferences.PrefsConstants
import com.mhss.app.preferences.domain.model.AiProvider
import com.mhss.app.preferences.domain.model.intPreferencesKey
import com.mhss.app.preferences.domain.model.stringPreferencesKey
import com.mhss.app.preferences.domain.use_case.GetPreferenceUseCase
import com.mhss.app.presentation.model.AiAction
import com.mhss.app.presentation.model.CanvasNode
import com.mhss.app.presentation.model.Connection
import com.mhss.app.presentation.model.NodeType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CanvasViewModel(
    private val summarizeText: SummarizeTextUseCase,
    private val extendText: ExtendTextUseCase,
    private val makeConcise: MakeConciseUseCase,
    private val generateImageUseCase: GenerateCanvasImageUseCase,
    private val getPreference: GetPreferenceUseCase,
) : ViewModel() {

    var nodes by mutableStateOf(listOf<CanvasNode>())
        private set

    var connections by mutableStateOf(listOf<Connection>())
        private set

    private lateinit var aiKey: String
    private lateinit var aiModel: String
    private lateinit var openaiURL: String

    private val aiProvider =
        getPreference(intPreferencesKey(PrefsConstants.AI_PROVIDER_KEY), AiProvider.None.id)
            .map { id -> AiProvider.entries.first { it.id == id } }
            .onEach { provider ->
                when (provider) {
                    AiProvider.OpenAI -> {
                        aiKey = getPreference(
                            stringPreferencesKey(PrefsConstants.OPENAI_KEY),
                            "",
                        ).first()
                        aiModel = getPreference(
                            stringPreferencesKey(PrefsConstants.OPENAI_MODEL_KEY),
                            AiConstants.OPENAI_DEFAULT_MODEL
                        ).first()
                        openaiURL = getPreference(
                            stringPreferencesKey(PrefsConstants.OPENAI_URL_KEY),
                            AiConstants.OPENAI_BASE_URL
                        ).first()
                    }
                    AiProvider.Gemini -> {
                        aiKey = getPreference(
                            stringPreferencesKey(PrefsConstants.GEMINI_KEY),
                            "",
                        ).first()
                        aiModel = getPreference(
                            stringPreferencesKey(PrefsConstants.GEMINI_MODEL_KEY),
                            AiConstants.GEMINI_DEFAULT_MODEL
                        ).first()
                        openaiURL = ""
                    }
                    else -> {
                        aiKey = ""
                        aiModel = ""
                        openaiURL = ""
                    }
                }
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, AiProvider.None)

    fun onEvent(event: CanvasEvent) {
        when (event) {
            is CanvasEvent.AddNode -> addNode(event.type, event.action)
            is CanvasEvent.UpdateInput -> updateInput(event.nodeId, event.index, event.value)
            is CanvasEvent.MoveNode -> moveNode(event.nodeId, event.position)
            is CanvasEvent.Connect -> connect(event.fromId, event.toId, event.targetInput)
            is CanvasEvent.RunNode -> runNode(event.nodeId)
        }
    }

    private fun addNode(type: NodeType, action: AiAction?) {
        val node = CanvasNode(type = type, action = action)
        nodes = nodes + node
    }

    private fun updateInput(id: String, index: Int, value: String) {
        nodes = nodes.map {
            if (it.id == id) {
                when (index) {
                    1 -> it.copy(input1 = value)
                    2 -> it.copy(input2 = value)
                    else -> it
                }
            } else it
        }
    }

    private fun moveNode(id: String, position: Offset) {
        nodes = nodes.map { if (it.id == id) it.copy(position = position) else it }
    }

    private fun connect(from: String, to: String, target: Int) {
        connections = connections + Connection(from, to, target)
    }

    private fun runNode(id: String) {
        val node = nodes.find { it.id == id } ?: return
        when (node.type) {
            NodeType.TEXT -> {
                val result = node.input1
                nodes = nodes.map { if (it.id == id) it.copy(output = result) else it }
            }
            NodeType.AI -> {
                viewModelScope.launch {
                    nodes = nodes.map { if (it.id == id) it.copy(loading = true, error = null) else it }
                    val input1 = getInput(node, 1)
                    val input2 = getInput(node, 2)
                    val result = when (node.action) {
                        AiAction.SUMMARIZE -> summarizeText(input1, aiKey, aiModel, aiProvider.value, openaiURL)
                        AiAction.EXTEND -> extendText(input1, aiKey, aiModel, aiProvider.value, openaiURL)
                        AiAction.CONCISE -> makeConcise(input1, aiKey, aiModel, aiProvider.value, openaiURL)
                        AiAction.GENERATE_IMAGE -> generateImageUseCase(input1, aiKey, aiModel, aiProvider.value, openaiURL)
                        null -> NetworkResult.Failure.UnexpectedError()
                    }
                    nodes = nodes.map { current ->
                        if (current.id == id) {
                            when (result) {
                                is NetworkResult.Success -> {
                                    if (node.action == AiAction.GENERATE_IMAGE) {
                                        current.copy(loading = false, imageUrl = result.data.url)
                                    } else {
                                        current.copy(loading = false, output = result.data)
                                    }
                                }
                                is NetworkResult.Failure -> current.copy(loading = false, error = result.toString())
                            }
                        } else current
                    }
                }
            }
            NodeType.MINDMAP -> {
                val input1 = getInput(node, 1)
                val input2 = getInput(node, 2)
                val result = "${input1.trim()} -> ${input2.trim()}"
                nodes = nodes.map { if (it.id == id) it.copy(output = result) else it }
            }
            NodeType.DRAWING -> {
                val input1 = getInput(node, 1)
                val result = "Drawing: ${input1.trim()}"
                nodes = nodes.map { if (it.id == id) it.copy(output = result) else it }
            }
            NodeType.WHITEBOARD -> {
                val input1 = getInput(node, 1)
                val result = "Whiteboard: ${input1.trim()}"
                nodes = nodes.map { if (it.id == id) it.copy(output = result) else it }
            }
            NodeType.WORKFLOW -> {
                val input1 = getInput(node, 1)
                val input2 = getInput(node, 2)
                val result = "Workflow: ${input1.trim()} -> ${input2.trim()}"
                nodes = nodes.map { if (it.id == id) it.copy(output = result) else it }
            }
        }
    }

    private fun getInput(node: CanvasNode, index: Int): String {
        val manual = if (index == 1) node.input1 else node.input2
        if (manual.isNotBlank()) return manual
        val conn = connections.lastOrNull { it.to == node.id && it.targetInput == index } ?: return ""
        val fromNode = nodes.find { it.id == conn.from } ?: return ""
        return fromNode.output.ifBlank { fromNode.imageUrl ?: "" }
    }
}
