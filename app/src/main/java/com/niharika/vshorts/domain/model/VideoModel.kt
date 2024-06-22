package com.niharika.vshorts.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoModel(
    @PrimaryKey val videoId: String,
    val title: String,
    val thumbnailUrl: String,
    val channelTitle:String,
    val publishTime:String
)