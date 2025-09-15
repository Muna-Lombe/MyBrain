package com.mhss.app.presentation.model

import androidx.compose.ui.geometry.Offset
import com.benasher44.uuid.uuid4

enum class NodeType {
    TEXT,
    AI,
    MINDMAP,
    DRAWING,
    WHITEBOARD,
    WORKFLOW
}

enum class AiAction { SUMMARIZE, EXTEND, CONCISE, GENERATE_IMAGE }

data class CanvasNode(
    val id: String = uuid4().toString(),
    val type: NodeType,
    val action: AiAction? = null,
    val position: Offset = Offset.Zero,
    val input1: String = "",
    val input2: String = "",
    val output: String = "",
    val imageUrl: String? = null,
    val loading: Boolean = false,
    val error: String? = null,
)

data class Connection(
    val from: String,
    val to: String,
    val targetInput: Int,
)
