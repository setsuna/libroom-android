package com.waseem.libroom

import android.media.MediaDrm
import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseem.libroom.core.usecase.NoParams
import com.waseem.libroom.feature.auth.domain.UpdateDeviceToken
import com.waseem.libroom.feature.root.device.DeviceInfo
import com.waseem.libroom.feature.root.device.DeviceType
import com.waseem.libroom.feature.root.device.GetDeviceInfo
import com.waseem.libroom.feature.root.device.UpdateDeviceInfo
import com.waseem.libroom.feature.root.domain.AuthState
import com.waseem.libroom.feature.root.domain.GetAuthState
import com.waseem.libroom.feature.root.domain.UpdateAuthState
import com.waseem.libroom.utils.EncryptionUtils
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
import kotlinx.coroutines.flow.first

@HiltViewModel
class MainViewModel @Inject constructor(
    getAuthState: GetAuthState,
    private val updateAuthState: UpdateAuthState,
    private val updateDeviceToken: UpdateDeviceToken
) : ViewModel() {
    private val _authState = mutableStateOf(AuthState.UNKNOWN)
    val authState : State<AuthState> = _authState

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
        viewModelScope.launch {
            try {
                updateDeviceToken(UpdateDeviceToken.Params(EncryptionUtils.getDeviceType().toString(),EncryptionUtils.getUniqueDeviceCode()))
                // If successful, keep the state as UNAUTHENTICATED to show LoginScreen
            } catch (e: Exception) {
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