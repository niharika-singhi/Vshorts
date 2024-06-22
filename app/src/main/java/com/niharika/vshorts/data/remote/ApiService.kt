package com.niharika.vshorts.data.remote

import com.niharika.vshorts.data.response.VideoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun getVideos(
        @Query("part") part: String = "snippet",
        @Query("q") query: String ,
        @Query("type") type: String = "video",
        @Query("key") apiKey: String
    ): VideoResponse
}