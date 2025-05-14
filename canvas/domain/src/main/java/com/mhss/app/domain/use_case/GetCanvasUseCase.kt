package com.mhss.app.domain.use_case

import com.mhss.app.database.entity.CanvasEntity
import com.mhss.app.domain.repository.CanvasRepository

class GetCanvasUseCaseclass GetCanvasUseCase(
    private val repository: CanvasRepository
) {
    suspend operator fun invoke(canvasId: Int): CanvasEntity? {
        return repository.getCanvas(canvasId)
    }
} {
}