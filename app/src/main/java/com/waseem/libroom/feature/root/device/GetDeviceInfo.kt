package com.waseem.libroom.feature.root.device

import com.waseem.libroom.core.usecase.NoParams
import com.waseem.libroom.core.usecase.ObservableUseCase

interface GetDeviceInfo : ObservableUseCase<DeviceInfo, NoParams>

class GetDeviceInfoImpl(
    private val deviceDataRepository: DeviceDataRepository
) : GetDeviceInfo {
    override fun invoke(params: NoParams) = deviceDataRepository.getDeviceInfo()
}