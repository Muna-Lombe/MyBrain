package com.mhss.app.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.consume
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mhss.app.presentation.model.AiAction
import com.mhss.app.presentation.model.NodeType
import com.mhss.app.ui.components.common.MyBrainAppBar
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

@Composable
fun CanvasScreen(viewModel: CanvasViewModel = koinViewModel()) {
    val nodes = viewModel.nodes
    val connections = viewModel.connections

    Scaffold(
        topBar = { MyBrainAppBar("Canvas") },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onEvent(CanvasEvent.AddNode(NodeType.AI, AiAction.SUMMARIZE)) }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        Box(Modifier.fillMaxSize().padding(paddingValues)) {
            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                connections.forEach { conn ->
                    val from = nodes.find { it.id == conn.from }
                    val to = nodes.find { it.id == conn.to }
                    if (from != null && to != null) {
                        drawLine(
                            color = Color.Gray,
                            start = from.position + Offset(80f, 40f),
                            end = to.position + Offset(0f, 40f),
                            strokeWidth = 4f
                        )
                    }
                }
            }
            nodes.forEach { node ->
                NodeItem(
                    node = node,
                    modifier = Modifier.offset { IntOffset(node.position.x.roundToInt(), node.position.y.roundToInt()) },
                    onDrag = { offset -> viewModel.onEvent(CanvasEvent.MoveNode(node.id, offset)) },
                    onInputChange = { index, value -> viewModel.onEvent(CanvasEvent.UpdateInput(node.id, index, value)) },
                    onRun = { viewModel.onEvent(CanvasEvent.RunNode(node.id)) }
                )
            }
        }
    }
}

@Composable
private fun NodeItem(
    node: com.mhss.app.presentation.model.CanvasNode,
    modifier: Modifier = Modifier,
    onDrag: (Offset) -> Unit,
    onInputChange: (Int, String) -> Unit,
    onRun: () -> Unit
) {
    var offset by remember { mutableStateOf(node.position) }
    Box(
        modifier
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offset += dragAmount
                    onDrag(offset)
                }
            }
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp)
    ) {
        when (node.type) {
            NodeType.TEXT -> TextFieldNode(node, onInputChange, onRun)
            NodeType.AI -> AiNode(node, onInputChange, onRun)
        }
    }
}

@Composable
private fun TextFieldNode(node: com.mhss.app.presentation.model.CanvasNode, onInput: (Int, String) -> Unit, onRun: () -> Unit) {
    var value by remember { mutableStateOf(node.input1) }
    androidx.compose.material3.TextField(
        value = value,
        onValueChange = { value = it; onInput(1, it) },
        label = { Text("Text") }
    )
}

@Composable
private fun AiNode(node: com.mhss.app.presentation.model.CanvasNode, onInput: (Int, String) -> Unit, onRun: () -> Unit) {
    var value1 by remember { mutableStateOf(node.input1) }
    androidx.compose.material3.TextField(
        value = value1,
        onValueChange = { value1 = it; onInput(1, it) },
        label = { Text("Input") }
    )
    if (node.loading) {
        Text("Loading...")
    } else {
        node.imageUrl?.let { Text(it) } ?: Text(node.output)
    }
    androidx.compose.material3.Button(onClick = onRun) { Text("Run") }
}
