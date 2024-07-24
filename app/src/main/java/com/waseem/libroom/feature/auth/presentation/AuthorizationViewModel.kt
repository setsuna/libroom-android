package com.waseem.libroom.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseem.libroom.feature.root.device.UpdateDeviceInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val updateDeviceInfo: UpdateDeviceInfo
) : ViewModel() {
    fun authorizeDevice(deviceToken: String) {
        viewModelScope.launch {
            try {
                updateDeviceInfo(UpdateDeviceInfo.Params(deviceInfo.deviceToken))
                // If successful, navigate back to login screen
            } catch (e: Exception) {
                // Handle authorization failure
            }
        }
    }
}