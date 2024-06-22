package com.niharika.vshorts.data.database

import com.niharika.vshorts.domain.model.VideoModel
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val videoDao: VideoDao) {
    suspend fun getVideos(): List<VideoModel> = videoDao.getAllVideos()
    suspend fun saveVideos(videos: List<VideoModel>) {
        videoDao.deleteAll()
        videoDao.insertAll(videos)
    }
}
