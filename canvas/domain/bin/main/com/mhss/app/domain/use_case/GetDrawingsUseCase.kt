package com.mhss.app.domain.use_case

import com.mhss.app.domain.model.CanvasDrawing
import com.mhss.app.domain.repository.CanvasRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetDrawingsUseCase(
    private val repository: CanvasRepository
) {
    operator fun invoke(): Flow<List<CanvasDrawing>> {
        return repository.getAllDrawings()
    }
} 