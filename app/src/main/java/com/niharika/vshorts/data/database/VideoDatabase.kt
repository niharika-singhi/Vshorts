package com.niharika.vshorts.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.niharika.vshorts.domain.model.VideoModel

@Database(entities = [VideoModel::class], version = 1, exportSchema = false)
abstract class VideoDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
}
