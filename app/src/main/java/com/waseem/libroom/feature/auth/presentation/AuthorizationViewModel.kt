package com.waseem.libroom.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseem.libroom.feature.auth.domain.UpdateDeviceToken
import com.waseem.libroom.feature.root.device.UniqueIdManager
import com.waseem.libroom.feature.root.device.UpdateDeviceInfo
import com.waseem.libroom.feature.root.domain.AuthState
import com.waseem.libroom.feature.root.domain.UpdateAuthState
import com.waseem.libroom.utils.EncryptionUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val updateAuthState: UpdateAuthState,
    private val updateDeviceToken: UpdateDeviceToken,
    private val uniqueIdManager: UniqueIdManager,
    private val updateDeviceInfo: UpdateDeviceInfo
) : ViewModel() {
    // 加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun authorizeDevice() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val deviceCode =  uniqueIdManager.getUniqueId()
                val result = updateDeviceToken(UpdateDeviceToken.Params(
                    EncryptionUtils.getDeviceType().toString(),
                    deviceCode
                ))
                if (result.isSuccess) {
                    //更新deviceInfo
                    val deviceInfo = result.getOrNull()
                    if (deviceInfo != null) {
                        updateDeviceInfo(UpdateDeviceInfo.Params(deviceInfo))
                    }
                    setAuthState(AuthState.UNAUTHENTICATED)
                } else {
                    setAuthState(AuthState.DEVICE_UNAUTHORIZED)
                }
            } catch (e: Exception) {
                setAuthState(AuthState.DEVICE_UNAUTHORIZED)
                // 可以在这里添加错误处理逻辑
            } finally {
                _isLoading.value = false
            }
        }
    }
    private fun setAuthState(authState: AuthState) {
        viewModelScope.launch {
            updateAuthState(UpdateAuthState.Params(authState))
        }
    }
}