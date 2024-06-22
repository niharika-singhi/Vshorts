package com.niharika.vshorts.domain.repository

import com.niharika.vshorts.data.response.VideoResponse
import com.niharika.vshorts.domain.model.VideoModel

interface VideoRepository {
    suspend fun getVideo(query:String, key:String): List<VideoModel>
}