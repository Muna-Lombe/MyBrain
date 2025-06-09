package com.mhss.app.presentation

import androidx.compose.ui.geometry.Offset
import com.mhss.app.presentation.model.AiAction
import com.mhss.app.presentation.model.NodeType

sealed interface CanvasEvent {
    data class AddNode(val type: NodeType, val action: AiAction? = null) : CanvasEvent
    data class UpdateInput(val nodeId: String, val index: Int, val value: String) : CanvasEvent
    data class MoveNode(val nodeId: String, val position: Offset) : CanvasEvent
    data class Connect(val fromId: String, val toId: String, val targetInput: Int) : CanvasEvent
    data class RunNode(val nodeId: String) : CanvasEvent
}
