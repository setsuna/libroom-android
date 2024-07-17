package com.waseem.libroom.feature.auth.data

import com.waseem.libroom.feature.auth.domain.AuthWithPasswordRepository
import com.waseem.libroom.feature.auth.domain.LoginCredentials
import com.waseem.libroom.feature.auth.domain.MeetingUser
import com.waseem.libroom.utils.EncryptionUtils
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class ApiAuthRepositoryImpl(
    private val httpClient: HttpClient
): AuthWithPasswordRepository {
    override suspend fun signIn(email: String, password: String): Result<MeetingUser> {
        return try {
            val response = httpClient.post("http://wzh.xunyidi.cn/api/user-service/user/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginCredentials(email, EncryptionUtils.md5(password)))
            }
            if (response.status.isSuccess()) {
                Result.success(response.body<MeetingUser>())
            } else {
                Result.failure(Exception("Login failed: ${response.status}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Boolean> {
        TODO("Not yet implemented")
    }
}