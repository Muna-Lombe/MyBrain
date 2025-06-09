package com.mhss.app.domain.repository

import com.mhss.app.domain.model.AiImage
import com.mhss.app.network.NetworkResult
import com.mhss.app.preferences.domain.model.AiProvider

interface CanvasRepository {

    suspend fun summarize(
        text: String,
        key: String,
        model: String,
        provider: AiProvider,
        baseUrl: String = "",
    ): NetworkResult<String>

    suspend fun extend(
        text: String,
        key: String,
        model: String,
        provider: AiProvider,
        baseUrl: String = "",
    ): NetworkResult<String>

    suspend fun makeConcise(
        text: String,
        key: String,
        model: String,
        provider: AiProvider,
        baseUrl: String = "",
    ): NetworkResult<String>

    suspend fun generateImage(
        prompt: String,
        key: String,
        model: String,
        provider: AiProvider,
        baseUrl: String = "",
    ): NetworkResult<AiImage>
}
