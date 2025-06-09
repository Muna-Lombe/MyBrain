package com.mhss.app.data.model.gemini

import kotlinx.serialization.Serializable

@Serializable
data class GeminiImageRequest(
    val contents: List<GeminiMessage>
)
