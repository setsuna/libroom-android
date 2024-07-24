package com.waseem.libroom.feature.root.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.waseem.libroom.feature.root.device.DeviceInfo
import com.waseem.libroom.feature.root.device.DeviceDataRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

object DeviceInfoKeys {
    val DEVICE_ID = intPreferencesKey("device_id")
    val DEVICE_TOKEN = stringPreferencesKey("device_token")
}

class DeviceInfoRepositoryImpl @Inject constructor(
    private val preferenceDataStore: DataStore<Preferences>,
    private val httpClient: HttpClient
) : DeviceDataRepository {
    override fun getDeviceInfo(): Flow<DeviceInfo> = preferenceDataStore.data.map { preferences ->
        val deviceId = preferences[DeviceInfoKeys.DEVICE_ID]?:0
        val token = preferences[DeviceInfoKeys.DEVICE_TOKEN]?:""
        DeviceInfo(deviceId, token)
    }

    override suspend fun setDeviceInfo(deviceInfo: DeviceInfo) {
        preferenceDataStore.edit { preferences ->
            preferences[DeviceInfoKeys.DEVICE_ID] = deviceInfo.deviceId
            preferences[DeviceInfoKeys.DEVICE_TOKEN] = deviceInfo.token
        }
    }
    override suspend fun getDeviceInfoByApi(): DeviceInfo {
        return try {
            val response = httpClient.post("/api/device-service/devices/token") {
                contentType(ContentType.Application.Json)
                setBody(mapOf(
                    "deviceCode" to deviceCode,
                    "deviceType" to deviceType.toString()
                ))
            }
            println("getDeviceToken response:${deviceType.name}| ${response.bodyAsText()}")
            if (response.status.isSuccess()) {
                response.body<DeviceInfo>()
            } else {
                throw Exception("获取设备令牌失败: ${response.bodyAsText()}")
            }
        } catch (e: Exception) {
            throw Exception("网络请求失败: ${e.message}")
        }
    }
}
