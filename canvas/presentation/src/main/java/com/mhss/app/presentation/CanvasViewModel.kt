package com.mhss.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhss.app.domain.model.CanvasDrawing
import com.mhss.app.domain.model.DrawPath
import com.mhss.app.domain.use_case.GetDrawingsUseCase
import com.mhss.app.domain.use_case.SaveDrawingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CanvasViewModel(
    private val saveDrawingUseCase: SaveDrawingUseCase,
    private val getDrawingsUseCase: GetDrawingsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CanvasState())
    val state = _state.asStateFlow()

    fun onEvent(event: CanvasEvent) {
        when (event) {
            is CanvasEvent.StartDrawing -> {
                _state.value = _state.value.copy(
                    currentPath = DrawPath(
                        points = listOf(event.point),
                        color = _state.value.currentColor,
                        strokeWidth = _state.value.strokeWidth
                    ),
                    isDrawing = true
                )
            }
            is CanvasEvent.Draw -> {
                val currentPath = _state.value.currentPath
                if (currentPath != null) {
                    _state.value = _state.value.copy(
                        currentPath = currentPath.copy(
                            points = currentPath.points + event.point
                        )
                    )
                }
            }
            is CanvasEvent.StopDrawing -> {
                val currentPath = _state.value.currentPath
                val currentDrawing = _state.value.currentDrawing
                if (currentPath != null) {
                    val updatedDrawing = currentDrawing?.copy(
                        paths = currentDrawing.paths + currentPath,
                        updatedAt = System.currentTimeMillis()
                    ) ?: CanvasDrawing(
                        title = "Untitled",
                        paths = listOf(currentPath),
                        backgroundColor = _state.value.backgroundColor
                    )
                    _state.value = _state.value.copy(
                        currentDrawing = updatedDrawing,
                        currentPath = null,
                        isDrawing = false
                    )
                }
            }
            is CanvasEvent.SetColor -> {
                _state.value = _state.value.copy(currentColor = event.color)
            }
            is CanvasEvent.SetStrokeWidth -> {
                _state.value = _state.value.copy(strokeWidth = event.width)
            }
            is CanvasEvent.SetBackgroundColor -> {
                _state.value = _state.value.copy(backgroundColor = event.color)
            }
            is CanvasEvent.SaveDrawing -> {
                viewModelScope.launch {
                    try {
                        _state.value = _state.value.copy(isSaving = true)
                        val drawing = _state.value.currentDrawing?.copy(
                            title = event.title
                        ) ?: return@launch
                        saveDrawingUseCase(drawing)
                        _state.value = _state.value.copy(
                            isSaving = false,
                            error = null
                        )
                    } catch (e: Exception) {
                        _state.value = _state.value.copy(
                            isSaving = false,
                            error = e.message
                        )
                    }
                }
            }
            is CanvasEvent.LoadDrawing -> {
                // TODO: Implement loading drawing
            }
            is CanvasEvent.ClearCanvas -> {
                _state.value = _state.value.copy(
                    currentDrawing = CanvasDrawing(
                        title = "Untitled",
                        paths = emptyList(),
                        backgroundColor = _state.value.backgroundColor
                    ),
                    currentPath = null,
                    isDrawing = false
                )
            }
        }
    }
} 