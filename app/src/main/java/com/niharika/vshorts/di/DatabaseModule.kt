package com.niharika.vshorts.di

import android.content.Context
import androidx.room.Room
import com.niharika.vshorts.data.database.LocalDataSource
import com.niharika.vshorts.data.database.VideoDao
import com.niharika.vshorts.data.database.VideoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): VideoDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            VideoDatabase::class.java,
            "video_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideVideoDao(database: VideoDatabase): VideoDao {
        return database.videoDao()
    }

  /*  @Provides
    @Singleton
    fun provideLocalDataSource(videoDao: VideoDao): LocalDataSource {
        return LocalDataSource(videoDao)
    }*/
}
