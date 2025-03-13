package com.mhss.app.domain.use_case

import com.mhss.app.domain.model.CanvasDrawing
import com.mhss.app.domain.repository.CanvasRepository
import org.koin.core.annotation.Factory

@Factory
class SaveDrawingUseCase(
    private val repository: CanvasRepository
) {
    suspend operator fun invoke(drawing: CanvasDrawing) {
        repository.saveDrawing(drawing)
    }
} 