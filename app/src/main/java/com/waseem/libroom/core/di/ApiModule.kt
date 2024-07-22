package com.waseem.libroom.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideBaseUrl(): String = "http://wzh.xunyidi.cn"

    @Singleton
    @Provides
    fun provideHttpClient(baseUrl: String): HttpClient {
        return HttpClient(Android){
            install(Logging){
                logger = Logger.DEFAULT
                level= LogLevel.ALL
            }
            install(DefaultRequest){
                url(baseUrl)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                //header("X-Api-Key",BuildConfig.API_KEY)
            }
            install(ContentNegotiation){
                json(Json)
            }
        }
    }
/**
    @Singleton
    @Provides
    fun provideApiService(httpClient: HttpClient):ApiService=ApiServiceImpl(httpClient)


    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.Default
 **/
}