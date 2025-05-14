package com.mhss.app.domain.use_case

import com.mhss.app.database.entity.CanvasEntity
import com.mhss.app.domain.repository.CanvasRepository

class InsertCanvasUseCase(
    private val repository: CanvasRepository
) {
    suspend operator fun invoke(canvas: CanvasEntity) {
        repository.insertCanvas(canvas)
    }
} {
}