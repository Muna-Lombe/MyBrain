package com.mhss.app.domain.repository

import com.mhss.app.domain.model.CanvasDrawing
import kotlinx.coroutines.flow.Flow

interface CanvasRepository {
    suspend fun saveDrawing(drawing: CanvasDrawing)
    suspend fun deleteDrawing(drawingId: String)
    suspend fun getDrawing(drawingId: String): CanvasDrawing?
    fun getAllDrawings(): Flow<List<CanvasDrawing>>
    suspend fun updateDrawing(drawing: CanvasDrawing)
} 