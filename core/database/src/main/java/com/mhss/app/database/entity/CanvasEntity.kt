package com.mhss.app.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mhss.app.domain.model.CanvasDrawing
import com.mhss.app.domain.model.DrawPath
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Entity(tableName = "canvas_table")
data class CanvasEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val title: String,
    val content: String
)

@Entity(tableName = "canvas_drawings")
data class CanvasDrawingEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    @ColumnInfo(name = "paths_json")
    val pathsJson: String,
    @ColumnInfo(name = "background_color")
    val backgroundColor: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
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
        fun fromDrawing(drawing: CanvasDrawing): CanvasDrawingEntity {
            return CanvasDrawingEntity(
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