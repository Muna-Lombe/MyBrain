package com.mhss.app.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mhss.app.domain.model.DrawPath
import com.mhss.app.domain.model.Point
import com.mhss.app.ui.R
import com.mhss.app.ui.components.common.MyBrainAppBar
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.graphics.toArgb






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanvasDetailScreen(
    navController: NavHostController,
    drawingId: String? = null,
    viewModel: CanvasDetailViewModel = koinViewModel()
) {
    var currentPath by remember { mutableStateOf(Path()) }
    var currentPoints by remember { mutableStateOf(listOf<Offset>()) }
    var paths by remember { mutableStateOf(listOf<DrawPath>()) }
    var currentColor by remember { mutableStateOf(Color.Black) }
    var currentStrokeWidth by remember { mutableStateOf(5f) }

    LaunchedEffect(drawingId) {
        if (drawingId != null) {
            viewModel.loadDrawing(drawingId)
        }
    }

    LaunchedEffect(viewModel.drawing) {
        viewModel.drawing?.let { drawing ->
            paths = drawing.paths
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Canvas") },
                actions = {
                    IconButton(onClick = {
                        viewModel.saveDrawing(paths)
                        navController.navigateUp()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.save_img),
                            contentDescription = "Save"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            currentPath = Path()
                            currentPoints = listOf(it)
                            currentPath.moveTo(it.x, it.y)
                        },
                        onDrag = { change, _ ->
                            val newPoint = change.position
                            currentPoints = currentPoints + newPoint
                            currentPath.lineTo(newPoint.x, newPoint.y)
                        },
                        onDragEnd = {
                            paths = paths + DrawPath(
                                points = currentPoints.map { offset -> Point(offset.x, offset.y) },
                                color = currentColor.toArgb(),
                                strokeWidth = currentStrokeWidth
                            )
                            currentPath = Path()
                            currentPoints = emptyList()
                        }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                paths.forEach { drawPath ->
                    val path = Path()
                    if (drawPath.points.isNotEmpty()) {
                        path.moveTo(drawPath.points.first().x, drawPath.points.first().y)
                        drawPath.points.drop(1).forEach { point ->
                            path.lineTo(point.x, point.y)
                        }
                    }
                    drawPath(
                        path = path,
                        color = Color(drawPath.color),
                        style = Stroke(width = drawPath.strokeWidth, cap = StrokeCap.Round)
                    )
                }
                if (currentPoints.isNotEmpty()) {
                    drawPath(
                        path = currentPath,
                        color = currentColor,
                        style = Stroke(width = currentStrokeWidth, cap = StrokeCap.Round)
                    )
                }
            }
        }
    }
} 