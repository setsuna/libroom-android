package com.waseem.libroom.feature.auth.data

import android.util.Log
import com.waseem.libroom.feature.auth.domain.AuthWithPWDRepository
import com.waseem.libroom.feature.auth.domain.LoginCredentials
import com.waseem.libroom.feature.auth.domain.Meeting
import com.waseem.libroom.feature.root.device.DeviceInfo
import com.waseem.libroom.utils.EncryptionUtils
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
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

    override suspend fun updateDeviceToken(
        deviceType: String,
        deviceCode: String
    ): Result<DeviceInfo> {
        return try {
            val response = httpClient.post("/api/device-service/devices/token") {
                contentType(ContentType.Application.Json)
                setBody(mapOf(
                    "code" to deviceCode,
                    "typeEnum" to deviceType
                ))
            }
            println("getDeviceToken response:${deviceType}| ${response.bodyAsText()}")
            if (response.status.isSuccess()) {
                Result.success(response.body<DeviceInfo>())
            } else {
                throw Exception("获取设备令牌失败: ${response.bodyAsText()}")
            }
        } catch (e: Exception) {
            throw Exception("网络请求失败: ${e.message}")
        }
    }
}