package com.waseem.libroom

import android.media.MediaDrm
import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseem.libroom.core.usecase.NoParams
import com.waseem.libroom.feature.root.device.DeviceInfo
import com.waseem.libroom.feature.root.device.DeviceType
import com.waseem.libroom.feature.root.device.GetDeviceInfo
import com.waseem.libroom.feature.root.device.UpdateDeviceInfo
import com.waseem.libroom.feature.root.domain.AuthState
import com.waseem.libroom.feature.root.domain.GetAuthState
import com.waseem.libroom.feature.root.domain.UpdateAuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import com.waseem.libroom.utils.EncryptionUtils.toHexString

@HiltViewModel
class MainViewModel @Inject constructor(
    getAuthState: GetAuthState,
    getDeviceInfo: GetDeviceInfo,
    private val updateAuthState: UpdateAuthState,
    private val updateDeviceInfo: UpdateDeviceInfo,
    private val httpClient: HttpClient
) : ViewModel() {
    private val _authState = mutableStateOf(AuthState.UNKNOWN)
    val authState : State<AuthState> = _authState



    init {
        viewModelScope.launch {
            getAuthState(NoParams).collect {
                _authState.value = it
            }
        }
    }

    fun setAuthState(authState: AuthState) {
        viewModelScope.launch {
            updateAuthState(UpdateAuthState.Params(authState))
        }
    }

    private suspend fun loadConfigurations() {
        val deviceCode = getUniqueDeviceCode()
        val deviceType = getDeviceType()
        try {
            val deviceInfo = getDeviceToken(deviceCode, deviceType)
            updateDeviceInfo(UpdateDeviceInfo.Params(deviceInfo))
        } catch (e: Exception) {
            // 处理错误，可能需要设置错误状态
            _initState.value = InitState.Error("获取设备信息失败: ${e.message}")
        }
    }
    private fun getUniqueDeviceCode(): String {
        val WIDEVINE_UUID = UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L)
        return try {
            var mediaDrm: MediaDrm? = null
            try {
                mediaDrm = MediaDrm(WIDEVINE_UUID)
                val widevineId = mediaDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID)
                widevineId.toHexString()
            } finally {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    mediaDrm?.close()
                } else {
                    @Suppress("DEPRECATION")
                    mediaDrm?.release()
                }
            }
        } catch (e: Exception) {
            // 如果获取 DRM ID 失败，可以回退到其他方法
            return "000"
        }
    }
    private fun getDeviceType(): DeviceType {
        // 根据设备特性判断类型，这里仅为示例
        return DeviceType.CLIENT_DESKCARD
    }

    private suspend fun getDeviceToken(deviceCode: String, deviceType: DeviceType): DeviceInfo {
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

    fun initialize() {
        viewModelScope.launch {
            _initState.value = InitState.Loading
            try {
                // 执行初始化操作，例如加载配置
                // 这里假设有一个 loadConfigurations() 挂起函数
                loadConfigurations()
                _initState.value = InitState.Completed
            } catch (e: Exception) {
                _initState.value = InitState.Error(e.message ?: "初始化失败")
            }
        }
    }
}