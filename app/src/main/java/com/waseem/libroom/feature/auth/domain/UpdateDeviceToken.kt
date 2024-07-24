package com.waseem.libroom.feature.auth.domain

import com.waseem.libroom.core.usecase.UseCase
import com.waseem.libroom.feature.root.device.DeviceInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

interface UpdateDeviceToken:UseCase<DeviceInfo,UpdateDeviceToken.Params>{
    data class Params(val deviceType:String,val deviceCode:String)
}

class UpdateDeviceTokenImpl(
    private val dispatcher: CoroutineDispatcher,
    private val authWithPWDRepository: AuthWithPWDRepository
):UpdateDeviceToken{
    override suspend fun invoke(params: UpdateDeviceToken.Params): Result<DeviceInfo> {
        return withContext(dispatcher){
            authWithPWDRepository.updateDeviceToken(params.deviceType,params.deviceCode)
        }
    }
}