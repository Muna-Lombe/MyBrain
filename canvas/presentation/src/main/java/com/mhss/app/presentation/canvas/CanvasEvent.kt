package com.mhss.app.presentation.canvas

import com.mhss.app.domain.model.Point

sealed class CanvasEvent {
    data class StartDrawing(val point: Point) : CanvasEvent()
    data class Draw(val point: Point) : CanvasEvent()
    object StopDrawing : CanvasEvent()
    data class SetColor(val color: Int) : CanvasEvent()
    data class SetStrokeWidth(val width: Float) : CanvasEvent()
    data class SetBackgroundColor(val color: Int) : CanvasEvent()
    data class SaveDrawing(val title: String) : CanvasEvent()
    data class LoadDrawing(val drawingId: String) : CanvasEvent()
    object ClearCanvas : CanvasEvent()
} 