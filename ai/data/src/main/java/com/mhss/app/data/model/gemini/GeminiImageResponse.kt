package com.mhss.app.data.model.gemini

import com.mhss.app.domain.model.AiImage
import kotlinx.serialization.Serializable

@Serializable
data class GeminiImageResponse(
    val candidates: List<GeminiCandidate>? = null,
    val error: GeminiError? = null,
)

fun GeminiImageResponse.toAiImage() =
    AiImage(candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "")
