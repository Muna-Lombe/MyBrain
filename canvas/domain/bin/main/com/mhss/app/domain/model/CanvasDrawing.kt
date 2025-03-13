package com.mhss.app.domain.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CanvasDrawing(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val paths: List<DrawPath>,
    val backgroundColor: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Serializable
data class DrawPath(
    val points: List<Point>,
    val color: Int,
    val strokeWidth: Float
)

@Serializable
data class Point(
    val x: Float,
    val y: Float
) 