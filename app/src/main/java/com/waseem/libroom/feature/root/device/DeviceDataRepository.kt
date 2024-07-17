package com.waseem.libroom.feature.root.device

import kotlinx.coroutines.flow.Flow

interface DeviceDataRepository {
    fun getDeviceInfo(): Flow<DeviceInfo>

    suspend fun setDeviceInfo(deviceInfo: DeviceInfo)
}