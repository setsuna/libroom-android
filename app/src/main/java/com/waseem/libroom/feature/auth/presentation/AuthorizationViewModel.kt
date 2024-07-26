package com.waseem.libroom.feature.auth.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseem.libroom.feature.auth.domain.UpdateDeviceToken
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
    private val updateDeviceToken: UpdateDeviceToken
) : ViewModel() {
    // 加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun authorizeDevice() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = updateDeviceToken(UpdateDeviceToken.Params(
                    EncryptionUtils.getDeviceType().toString(),
                    EncryptionUtils.getUniqueDeviceCode()
                ))

                if (result.isSuccess) {
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