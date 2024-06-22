package com.niharika.vshorts.di

import com.niharika.vshorts.data.database.LocalDataSource
import com.niharika.vshorts.data.remote.ApiService
import com.niharika.vshorts.data.repository.VideoRepositoryImpl
import com.niharika.vshorts.domain.repository.VideoRepository
import com.niharika.vshorts.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideVideoRepository(remoteDataSource: ApiService,localDataSource: LocalDataSource) : VideoRepository = VideoRepositoryImpl(remoteDataSource,localDataSource)
}

@Module
@InstallIn(SingletonComponent::class) // Install in the appropriate component
object ApiKeyModule {
    @Provides
    @Singleton // Depending on your scope requirements
    fun provideApiKey(): String {
        return Constants.API_KEY // Replace with how you provide your API key
    }
}