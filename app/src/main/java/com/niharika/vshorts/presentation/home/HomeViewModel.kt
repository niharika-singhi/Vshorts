package com.niharika.vshorts.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niharika.vshorts.domain.model.Resource
import com.niharika.vshorts.domain.model.VideoModel
import com.niharika.vshorts.domain.usecases.GetVideosUseCase
import com.niharika.vshorts.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(private val getVideosUseCase: GetVideosUseCase): ViewModel() {

    var videoList = listOf<VideoModel>()

    private val _videosState = MutableStateFlow<Resource<List<VideoModel>>>(Resource.Loading)
    val videosState: StateFlow<Resource<List<VideoModel>>> = _videosState

    init {
        getVideos()
    }

     fun getVideos() {
        getVideosUseCase.getVideos(Constants.DEFAULT_QUERY).onEach { result ->
            when (result) {
                Resource.Loading -> _videosState.emit(Resource.Loading)
                is Resource.Success -> {
                    result.data.let {
                        _videosState.emit(Resource.Success(it))
                        videoList = it
                        println(it)
                    }
                }
                is Resource.Error -> _videosState.emit(Resource.Error(errorMessage = result.errorMessage))
            }
        }.launchIn(viewModelScope)
    }
}