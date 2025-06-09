package com.mhss.app.presentation

sealed interface CanvasEvent {
    data class UpdateText(val text: String) : CanvasEvent
    data class UpdatePrompt(val prompt: String) : CanvasEvent
    data object Summarize : CanvasEvent
    data object Extend : CanvasEvent
    data object MakeConcise : CanvasEvent
    data object GenerateImage : CanvasEvent
    data object AiResultHandled : CanvasEvent
}
