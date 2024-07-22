package com.waseem.libroom.feature.auth.data

import android.util.Log
import com.waseem.libroom.core.di.ApiModule.provideHttpClient
import com.waseem.libroom.feature.auth.domain.AuthWithPWDRepository
import com.waseem.libroom.feature.auth.domain.LoginCredentials
import com.waseem.libroom.feature.auth.domain.Meeting
import com.waseem.libroom.feature.auth.domain.MeetingUser
import com.waseem.libroom.utils.EncryptionUtils
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.date
import io.ktor.http.isSuccess
import javax.inject.Inject

class AuthWithPWDRepositoryImpl @Inject constructor(
    private var httpClient: HttpClient
): AuthWithPWDRepository {
    override suspend fun signInPWD(username: String, password: String): Result<Meeting> {
        val loginCredentials = LoginCredentials("abc",username, EncryptionUtils.encryptPassword(password))
        println("Request body: ${loginCredentials.toString()}")
        Log.d("TAG", "Your message here $loginCredentials")
        return try {
            val response = httpClient.post("/api/meeting-service/meeting_user_token/login") {
                contentType(ContentType.Application.Json)
                setBody(loginCredentials)
            }
            if (response.status.isSuccess()) {
                Result.success(response.body<Meeting>())
            } else {
                val errorBody = response.bodyAsText()
                println("Error response: $errorBody")
                Result.failure(Exception("Login failed: ${response.bodyAsText()}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }


    override suspend fun signOutPWD(): Result<Boolean> {
        return Result.success(true)
    }
}