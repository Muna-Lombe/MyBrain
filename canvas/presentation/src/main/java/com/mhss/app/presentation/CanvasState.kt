package com.mhss.app.presentation

import com.mhss.app.domain.model.CanvasDrawing
import com.mhss.app.domain.model.DrawPath

data class CanvasState(
    val currentDrawing: CanvasDrawing? = null,
    val currentPath: DrawPath? = null,
    val currentColor: Int = 0xFF000000.toInt(), // Default black color
    val strokeWidth: Float = 5f,
    val backgroundColor: Int = 0xFFFFFFFF.toInt(), // Default white background
    val isDrawing: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null
) 