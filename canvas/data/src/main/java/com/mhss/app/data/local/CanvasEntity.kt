package com.mhss.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mhss.app.domain.model.CanvasDrawing
import com.mhss.app.domain.model.DrawPath
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "canvas_drawings")
data class CanvasEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val pathsJson: String,
    val backgroundColor: Int,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toDrawing(): CanvasDrawing {
        return CanvasDrawing(
            id = id,
            title = title,
            paths = Json.decodeFromString<List<DrawPath>>(pathsJson),
            backgroundColor = backgroundColor,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun fromDrawing(drawing: CanvasDrawing): CanvasEntity {
            return CanvasEntity(
                id = drawing.id,
                title = drawing.title,
                pathsJson = Json.encodeToString(drawing.paths),
                backgroundColor = drawing.backgroundColor,
                createdAt = drawing.createdAt,
                updatedAt = drawing.updatedAt
            )
        }
    }
} 