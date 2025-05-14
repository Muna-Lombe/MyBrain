package com.mhss.app.domain.use_case

import com.mhss.app.database.entity.CanvasEntity
import com.mhss.app.domain.repository.CanvasRepository
import kotlinx.coroutines.flow.Flow

class GetAllCanvasUseCaseclass GetAllCanvasesUseCase(
    private val repository: CanvasRepository = TODO()
) {
    operator fun invoke(): Flow<List<CanvasEntity>> {
        return repository.getAllCanvases()
    }
} {
}