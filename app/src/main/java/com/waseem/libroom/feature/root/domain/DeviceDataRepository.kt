package com.waseem.libroom.feature.root.domain

import com.waseem.libroom.feature.root.device.DeviceInfo
import kotlinx.coroutines.flow.Flow

interface DeviceDataRepository {
    fun getDeviceState(): Flow<DeviceInfo>

    suspend fun setDeviceState(deviceInfo: DeviceInfo)
}