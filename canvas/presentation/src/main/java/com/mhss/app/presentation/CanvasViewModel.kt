package com.mhss.app.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var text by mutableStateOf("")
        private set
    var prompt by mutableStateOf("")
        private set

    var aiState by mutableStateOf(AiState())
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
            is CanvasEvent.UpdateText -> text = event.text
            is CanvasEvent.UpdatePrompt -> prompt = event.prompt
            CanvasEvent.Summarize -> generateText {
                summarizeText(text, aiKey, aiModel, aiProvider.value, openaiURL)
            }
            CanvasEvent.Extend -> generateText {
                extendText(text, aiKey, aiModel, aiProvider.value, openaiURL)
            }
            CanvasEvent.MakeConcise -> generateText {
                makeConcise(text, aiKey, aiModel, aiProvider.value, openaiURL)
            }
            CanvasEvent.GenerateImage -> generateImage()
            CanvasEvent.AiResultHandled -> aiState = aiState.copy(showAiSheet = false)
        }
    }

    private fun generateText(block: suspend () -> NetworkResult<String>) {
        viewModelScope.launch {
            aiState = aiState.copy(loading = true, result = null, error = null, showAiSheet = true)
            when (val result = block()) {
                is NetworkResult.Success -> aiState = aiState.copy(loading = false, result = result.data)
                is NetworkResult.Failure -> aiState = aiState.copy(loading = false, error = result)
            }
        }
    }

    private fun generateImage() {
        viewModelScope.launch {
            aiState = aiState.copy(loading = true, imageUrl = null, error = null, showAiSheet = true)
            val result = generateImageUseCase(
                prompt.ifBlank { text },
                aiKey,
                aiModel,
                aiProvider.value,
                openaiURL
            )
            aiState = when (result) {
                is NetworkResult.Success -> aiState.copy(loading = false, imageUrl = result.data.url)
                is NetworkResult.Failure -> aiState.copy(loading = false, error = result)
            }
        }
    }

    data class AiState(
        val loading: Boolean = false,
        val result: String? = null,
        val imageUrl: String? = null,
        val error: NetworkResult.Failure? = null,
        val showAiSheet: Boolean = false,
    )
}
