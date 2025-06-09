package com.mhss.app.data.model.openai

import kotlinx.serialization.Serializable

@Serializable
data class OpenaiImageRequest(
    val prompt: String,
    val model: String
)
