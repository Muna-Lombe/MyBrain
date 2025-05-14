package com.mhss.app.domain.use_case
import com.mhss.app.domain.repository.CanvasRepository

class DeleteCanvasUseCase (
    private val repository: CanvasRepository
) {
    suspend operator fun invoke(canvasId: Int) {
        return repository.deleteCanvas(canvasId)
    }
}