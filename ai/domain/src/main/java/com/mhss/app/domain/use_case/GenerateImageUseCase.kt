package com.mhss.app.domain.use_case

import com.mhss.app.domain.AiConstants
import com.mhss.app.domain.model.AiImage
import com.mhss.app.domain.repository.AiApi
import com.mhss.app.network.NetworkResult
import com.mhss.app.preferences.domain.model.AiProvider
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.io.IOException

@Single
class GenerateImageUseCase(
    @Named("openaiApi") private val openai: AiApi,
    @Named("geminiApi") private val gemini: AiApi,
) {
    suspend operator fun invoke(
        prompt: String,
        key: String,
        model: String,
        provider: AiProvider,
        baseURL: String = "",
    ): NetworkResult<AiImage> {
        if (key.isBlank()) return NetworkResult.InvalidKey
        return try {
            when (provider) {
                AiProvider.OpenAI -> openai.generateImage(baseURL, prompt, model, key)
                AiProvider.Gemini -> gemini.generateImage(AiConstants.GEMINI_BASE_URL, prompt, model, key)
                else -> throw IllegalStateException("No AI provider is chosen")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            NetworkResult.InternetError
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.OtherError()
        }
    }
}
