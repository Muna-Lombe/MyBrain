package com.mhss.app.data

import com.mhss.app.data.local.CanvasDao
import com.mhss.app.database.entity.CanvasEntity
import com.mhss.app.domain.model.CanvasDrawing
import com.mhss.app.domain.repository.CanvasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class CanvasRepositoryImpl(
    private val dao: CanvasDao
) : CanvasRepository {
    override fun getAllCanvases(): Flow<List<CanvasEntity>> {
        return dao.getAllCanvases()
    }

    override suspend fun getCanvas(canvasId: Int): CanvasEntity? {
        return dao.getCanvas(canvasId)
    }

    override suspend fun insertCanvas(canvas: CanvasEntity) {
        dao.insertCanvas(canvas)
    }

    override suspend fun deleteCanvas(canvasId: Int) {
        dao.deleteCanvas(canvasId)
    }
    override suspend fun saveDrawing(drawing: CanvasDrawing) {
        dao.insertDrawing(CanvasEntity.fromDrawing(drawing))
    }

    override suspend fun deleteDrawing(drawingId: String) {
        dao.deleteDrawing(drawingId)
    }

    override suspend fun getDrawing(drawingId: String): CanvasDrawing? {
        return dao.getDrawing(drawingId)?.toDrawing()
    }

    override fun getAllDrawings(): Flow<List<CanvasDrawing>> {
        return dao.getAllDrawings().map { entities ->
            entities.map { it.toDrawing() }
        }
    }

    override suspend fun updateDrawing(drawing: CanvasDrawing) {
        dao.updateDrawing(CanvasEntity.fromDrawing(drawing))
    }
} 