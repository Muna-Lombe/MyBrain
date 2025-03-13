package com.mhss.app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mhss.app.database.converters.DBConverters
import com.mhss.app.database.dao.*
import com.mhss.app.database.entity.*
import com.mhss.app.database.entity.CanvasEntity

@Database(
    entities = [
        NoteEntity::class,
        TaskEntity::class,
        DiaryEntryEntity::class,
        BookmarkEntity::class,
        AlarmEntity::class,
        NoteFolderEntity::class,
        CanvasEntity::class
    ],
    version = 5
)
@TypeConverters(DBConverters::class)
abstract class MyBrainDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun taskDao(): TaskDao
    abstract fun diaryDao(): DiaryDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun alarmDao(): AlarmDao
    abstract fun canvasDao(): CanvasDao

    companion object {
        const val DATABASE_NAME = "by_brain_db"
    }
}