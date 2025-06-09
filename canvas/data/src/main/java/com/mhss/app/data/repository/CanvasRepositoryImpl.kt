package com.mhss.app.data.repository

import com.mhss.app.domain.conciseNotePrompt
import com.mhss.app.domain.extendNotePrompt
import com.mhss.app.domain.summarizeNotePrompt
import com.mhss.app.domain.model.AiImage
import com.mhss.app.domain.repository.CanvasRepository
import com.mhss.app.domain.use_case.GenerateImageUseCase
import com.mhss.app.domain.use_case.SendAiPromptUseCase
import com.mhss.app.network.NetworkResult
import com.mhss.app.preferences.domain.model.AiProvider
import org.koin.core.annotation.Single

@Single
class CanvasRepositoryImpl(
    private val sendPrompt: SendAiPromptUseCase,
    private val generateImageUseCase: GenerateImageUseCase,
) : CanvasRepository {

    override suspend fun summarize(
        text: String,
        key: String,
        model: String,
        provider: AiProvider,
        baseUrl: String
    ): NetworkResult<String> =
        sendPrompt(text.summarizeNotePrompt, key, model, provider, baseUrl)

    override suspend fun extend(
        text: String,
        key: String,
        model: String,
        provider: AiProvider,
        baseUrl: String
    ): NetworkResult<String> =
        sendPrompt(text.extendNotePrompt, key, model, provider, baseUrl)

    override suspend fun makeConcise(
        text: String,
        key: String,
        model: String,
        provider: AiProvider,
        baseUrl: String
    ): NetworkResult<String> =
        sendPrompt(text.conciseNotePrompt, key, model, provider, baseUrl)

    override suspend fun generateImage(
        prompt: String,
        key: String,
        model: String,
        provider: AiProvider,
        baseUrl: String
    ): NetworkResult<AiImage> =
        generateImageUseCase(prompt, key, model, provider, baseUrl)
}
