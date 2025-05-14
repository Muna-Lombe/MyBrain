package com.mhss.app.domain.repository

import com.mhss.app.database.entity.CanvasEntity
import com.mhss.app.domain.model.CanvasDrawing
import kotlinx.coroutines.flow.Flow

interface CanvasRepository {
    fun getAllCanvases(): Flow<List<CanvasEntity>>
    suspend fun getCanvas(canvasId: Int): CanvasEntity?
    suspend fun insertCanvas(canvas: CanvasEntity)
    suspend fun deleteCanvas(canvasId: Int)
    suspend fun saveDrawing(drawing: CanvasDrawing)
    suspend fun deleteDrawing(drawingId: String)
    suspend fun getDrawing(drawingId: String): CanvasDrawing?
    fun getAllDrawings(): Flow<List<CanvasDrawing>>
    suspend fun updateDrawing(drawing: CanvasDrawing)
} 