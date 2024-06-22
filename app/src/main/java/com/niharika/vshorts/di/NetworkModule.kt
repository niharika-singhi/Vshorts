package com.niharika.vshorts.di

import com.niharika.vshorts.data.remote.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val APP_BASE_URL = "https://www.googleapis.com/youtube/v3/"

private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val client = OkHttpClient.Builder().apply {
    addInterceptor(interceptor)
    connectTimeout(120, TimeUnit.SECONDS)
    writeTimeout(120, TimeUnit.SECONDS)
    readTimeout(120, TimeUnit.SECONDS)
}.build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl(APP_BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(client)
    .build()


@InstallIn(SingletonComponent::class)
@Module
object Network {
    @Singleton
    @Provides
    fun provideNetworkApi(): Retrofit {
        return retrofit
    }
}
@Module
@InstallIn(SingletonComponent::class)
object ApiInterfaceModule {
    @Provides
    @Singleton
    fun provideHomeApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}

