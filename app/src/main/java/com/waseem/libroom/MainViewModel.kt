package com.waseem.libroom

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseem.libroom.core.usecase.NoParams
import com.waseem.libroom.feature.auth.domain.UpdateDeviceToken
import com.waseem.libroom.feature.root.device.UniqueIdManager
import com.waseem.libroom.feature.root.device.UpdateDeviceInfo
import com.waseem.libroom.feature.root.domain.AuthState
import com.waseem.libroom.feature.root.domain.GetAuthState
import com.waseem.libroom.feature.root.domain.UpdateAuthState
import com.waseem.libroom.utils.EncryptionUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getAuthState: GetAuthState,
    private val updateAuthState: UpdateAuthState,
    private val updateDeviceToken: UpdateDeviceToken,
    private val updateDeviceInfo: UpdateDeviceInfo,
    private val uniqueIdManager: UniqueIdManager
) : ViewModel() {
    private val _authState = mutableStateOf(AuthState.UNKNOWN)
    val authState : State<AuthState> = _authState
    private var isCheckingAuthorization = false

    init {
        viewModelScope.launch {
            getAuthState(NoParams).collect {
                _authState.value = it
                if(it == AuthState.UNAUTHENTICATED){
                    checkDeviceAuthorization()
                }
            }
        }
    }

    fun checkDeviceAuthorization() {
        if (isCheckingAuthorization) return
        isCheckingAuthorization = true

        viewModelScope.launch {
            var deviceCode = EncryptionUtils.getWidevineId()
            if(deviceCode.isEmpty()){
                deviceCode = uniqueIdManager.getUniqueId()
            }
            val result = updateDeviceToken(
                UpdateDeviceToken.Params(
                    EncryptionUtils.getDeviceType().toString(),
                    deviceCode
                )
            )
            // If successful, keep the state as UNAUTHENTICATED to show LoginScreen
            when {
                result.isSuccess -> {
                    val deviceInfo = result.getOrNull()
                    deviceInfo?.let {
                        val updateResult = updateDeviceInfo(UpdateDeviceInfo.Params(it))
                        if (updateResult.isSuccess) {
                            // 如果需要，可以在这里设置认证状态
                            setAuthState(AuthState.UNAUTHENTICATED)
                        } else {
                            // 处理更新设备信息失败的情况
                            setAuthState(AuthState.DEVICE_UNAUTHORIZED)
                        }
                    } ?: run {
                        // 处理 deviceInfo 为 null 的情况
                        setAuthState(AuthState.DEVICE_UNAUTHORIZED)
                    }
                }
            }
            result.onFailure {
                // If token update fails, set state to DEVICE_UNAUTHORIZED
                setAuthState(AuthState.DEVICE_UNAUTHORIZED)
            }
        }
    }

    fun setAuthState(authState: AuthState) {
        viewModelScope.launch {
            updateAuthState(UpdateAuthState.Params(authState))
        }
    }
}