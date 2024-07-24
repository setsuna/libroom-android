package com.waseem.libroom.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseem.libroom.feature.auth.domain.UpdateDeviceToken
import com.waseem.libroom.feature.root.device.UpdateDeviceInfo
import com.waseem.libroom.utils.EncryptionUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val updateDeviceToken: UpdateDeviceToken
) : ViewModel() {
    fun authorizeDevice(deviceToken: String) {
        viewModelScope.launch {
            try {
                updateDeviceToken(UpdateDeviceToken.Params(EncryptionUtils.getDeviceType().toString(),EncryptionUtils.getUniqueDeviceCode()))
                // If successful, navigate back to login screen
            } catch (e: Exception) {
                // Handle authorization failure
            }
        }
    }
}