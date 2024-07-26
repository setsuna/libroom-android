package com.waseem.libroom.feature.auth.data

import android.util.Log
import com.waseem.libroom.core.usecase.ApiResponse
import com.waseem.libroom.core.usecase.NoParams
import com.waseem.libroom.core.usecase.toResult
import com.waseem.libroom.feature.auth.domain.AuthWithPWDRepository
import com.waseem.libroom.feature.auth.domain.LoginCredentials
import com.waseem.libroom.feature.auth.domain.Meeting
import com.waseem.libroom.feature.root.device.DeviceInfo
import com.waseem.libroom.feature.root.device.GetDeviceInfo
import com.waseem.libroom.utils.EncryptionUtils
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthWithPWDRepositoryImpl @Inject constructor(
    private var httpClient: HttpClient,
    private var getDeviceInfo: GetDeviceInfo
): AuthWithPWDRepository {
    override suspend fun signInPWD(username: String, password: String): Result<Meeting> {
        val deviceInfo = getDeviceInfo(NoParams).first()
        val loginCredentials = LoginCredentials(deviceInfo.token,username, EncryptionUtils.encryptPassword(password))
        println("Request body: ${loginCredentials.toString()}")
        Log.d("TAG", "Your message here $loginCredentials")
        return try {
            val response = httpClient.post("/api/meeting-service/meeting_user_token/login") {
                contentType(ContentType.Application.Json)
                setBody(loginCredentials)
            }
            val responseBody = response.body<ApiResponse<Meeting>>()
            responseBody.toResult()
        } catch (e: Exception) {
            Result.failure(Exception("网络请求失败: ${e.message}"))
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
            println("getDeviceToken response:${deviceCode}| ${response.bodyAsText()}")

            val responseBody = response.body<ApiResponse<DeviceInfo>>()
            responseBody.toResult()

        } catch (e: Exception) {
            Result.failure(Exception("网络请求失败: ${e.message}"))
        }
    }
}