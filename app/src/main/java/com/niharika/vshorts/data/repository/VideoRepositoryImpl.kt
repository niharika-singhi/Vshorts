package com.niharika.vshorts.data.repository

import com.niharika.vshorts.data.database.LocalDataSource
import com.niharika.vshorts.data.mapper.toVideoList
import com.niharika.vshorts.data.remote.ApiService
import com.niharika.vshorts.data.response.VideoResponse
import com.niharika.vshorts.domain.model.VideoModel
import com.niharika.vshorts.domain.repository.VideoRepository
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(private val remoteDataSource: ApiService,
                                              private val localDataSource: LocalDataSource):
    VideoRepository {
    override suspend fun getVideo(query: String, key: String): List<VideoModel> {
        return try {
            val videoResponse = remoteDataSource.getVideos(query = query, apiKey = key)
            val videoEntities = videoResponse.toVideoList()
            localDataSource.saveVideos(videoEntities)
            videoEntities
        } catch (exception: Exception) {
            localDataSource.getVideos()
        }
    }
}
