package com.mhss.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CanvasDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrawing(drawing: CanvasEntity)

    @Query("DELETE FROM canvas_drawings WHERE id = :drawingId")
    suspend fun deleteDrawing(drawingId: String)

    @Query("SELECT * FROM canvas_drawings WHERE id = :drawingId")
    suspend fun getDrawing(drawingId: String): CanvasEntity?

    @Query("SELECT * FROM canvas_drawings ORDER BY updatedAt DESC")
    fun getAllDrawings(): Flow<List<CanvasEntity>>

    @Update
    suspend fun updateDrawing(drawing: CanvasEntity)
} 