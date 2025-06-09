package com.mhss.app.data.model.openai

import com.mhss.app.domain.model.AiImage
import kotlinx.serialization.Serializable

@Serializable
data class OpenaiImageResponse(
    val created: Long? = null,
    val data: List<ImageData>? = null,
    val error: OpenaiError? = null,
)

@Serializable
data class ImageData(
    val url: String? = null,
    val b64_json: String? = null,
)

fun OpenaiImageResponse.toAiImage() =
    AiImage(data?.firstOrNull()?.url ?: "")
