package com.waseem.libroom.feature.root.device

import com.waseem.libroom.core.usecase.UseCase
import com.waseem.libroom.feature.root.domain.UpdateAuthState

interface UpdateDeviceInfo : UseCase<Unit, UpdateDeviceInfo.Params> {
    data class Params(val deviceInfo: DeviceInfo)
}

class UpdateDeviceInfoImpl(
    private val deviceDataRepository: DeviceDataRepository
) : UpdateDeviceInfo {
    override suspend fun invoke(params: UpdateAuthState.Params): Result<Unit> {
        return Result.success(userPreferenceRepository.setAuthState(params.authState))
    }
}