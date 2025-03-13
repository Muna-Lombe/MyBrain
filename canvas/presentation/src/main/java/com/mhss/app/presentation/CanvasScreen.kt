package com.mhss.app.presentation.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mhss.app.domain.model.Point
import com.mhss.app.ui.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun CanvasScreen(
    viewModel: CanvasViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var showColorPicker by remember { mutableStateOf(false) }
    var showStrokeWidthPicker by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(state.backgroundColor))
    ) {
        // Top Bar with tools
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { showColorPicker = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.color_lens_img),
                    contentDescription = "Pick Color",
                    tint = Color(state.currentColor)
                )
            }
            IconButton(onClick = { showStrokeWidthPicker = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.brush_img),
                    contentDescription = "Stroke Width"
                )
            }
            IconButton(onClick = { viewModel.onEvent(CanvasEvent.ClearCanvas) }) {
                Icon(
                    painter = painterResource(id = R.drawable.clear_img),
                    contentDescription = "Clear Canvas"
                )
            }
            IconButton(onClick = { showSaveDialog = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.save_img),
                    contentDescription = "Save Drawing"
                )
            }
        }

        // Drawing Canvas
        Canvas(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            viewModel.onEvent(
                                CanvasEvent.StartDrawing(
                                    Point(offset.x, offset.y)
                                )
                            )
                        },
                        onDrag = { _, dragAmount ->
                            viewModel.onEvent(
                                CanvasEvent.Draw(
                                    Point(dragAmount.x, dragAmount.y)
                                )
                            )
                        },
                        onDragEnd = {
                            viewModel.onEvent(CanvasEvent.StopDrawing)
                        }
                    )
                }
        ) {
            // Draw completed paths
            state.currentDrawing?.paths?.forEach { drawPath ->
                val path = Path()
                drawPath.points.forEachIndexed { index, point ->
                    if (index == 0) {
                        path.moveTo(point.x, point.y)
                    } else {
                        path.lineTo(point.x, point.y)
                    }
                }
                drawPath(
                    path = path,
                    color = Color(drawPath.color),
                    style = Stroke(
                        width = drawPath.strokeWidth,
                        cap = StrokeCap.Round
                    )
                )
            }

            // Draw current path
            state.currentPath?.let { drawPath ->
                val path = Path()
                drawPath.points.forEachIndexed { index, point ->
                    if (index == 0) {
                        path.moveTo(point.x, point.y)
                    } else {
                        path.lineTo(point.x, point.y)
                    }
                }
                drawPath(
                    path = path,
                    color = Color(drawPath.color),
                    style = Stroke(
                        width = drawPath.strokeWidth,
                        cap = StrokeCap.Round
                    )
                )
            }
        }
    }

    // Color Picker Dialog
    if (showColorPicker) {
        AlertDialog(
            onDismissRequest = { showColorPicker = false },
            title = { Text("Pick Color") },
            text = {
                // Simple color picker with predefined colors
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(
                        Color.Black,
                        Color.Red,
                        Color.Green,
                        Color.Blue,
                        Color.Yellow
                    ).forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(color)
                                .clickable {
                                    viewModel.onEvent(
                                        CanvasEvent.SetColor(color.toArgb())
                                    )
                                    showColorPicker = false
                                }
                        )
                    }
                }
            },
            confirmButton = {}
        )
    }

    // Stroke Width Picker Dialog
    if (showStrokeWidthPicker) {
        AlertDialog(
            onDismissRequest = { showStrokeWidthPicker = false },
            title = { Text("Stroke Width") },
            text = {
                Slider(
                    value = state.strokeWidth,
                    onValueChange = { width ->
                        viewModel.onEvent(CanvasEvent.SetStrokeWidth(width))
                    },
                    valueRange = 1f..20f
                )
            },
            confirmButton = {
                TextButton(onClick = { showStrokeWidthPicker = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Save Dialog
    if (showSaveDialog) {
        var title by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save Drawing") },
            text = {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(CanvasEvent.SaveDrawing(title))
                        showSaveDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Error Snackbar
    state.error?.let { error ->
        LaunchedEffect(error) {
            // Show error snackbar
        }
    }
} 