package com.mhss.app.domain.use_case

import com.mhss.app.domain.model.AiImage
import com.mhss.app.domain.repository.CanvasRepository
import com.mhss.app.network.NetworkResult
import com.mhss.app.preferences.domain.model.AiProvider
import org.koin.core.annotation.Single

@Single
class GenerateCanvasImageUseCase(
    private val repository: CanvasRepository
) {
    suspend operator fun invoke(
        prompt: String,
        key: String,
        model: String,
        provider: AiProvider,
        baseUrl: String = "",
    ): NetworkResult<AiImage> = repository.generateImage(prompt, key, model, provider, baseUrl)
}
