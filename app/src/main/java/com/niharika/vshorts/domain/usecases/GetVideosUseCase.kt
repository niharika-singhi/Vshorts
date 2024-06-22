package com.niharika.vshorts.domain.usecases

import com.niharika.vshorts.data.mapper.toVideoList
import com.niharika.vshorts.domain.model.Resource
import com.niharika.vshorts.domain.model.VideoModel
import com.niharika.vshorts.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(
    private val videosRepository: VideoRepository,
    private val apiKey: String, // Inject the API key here

) {
    fun getVideos(query: String): Flow<Resource<List<VideoModel>>> = flow {

        try {
            emit(Resource.Loading)
            emit(Resource.Success(videosRepository.getVideo(query,apiKey)))
        } catch (e: Exception) {
            emit(Resource.Error("Error !!"))
        }
    }
}