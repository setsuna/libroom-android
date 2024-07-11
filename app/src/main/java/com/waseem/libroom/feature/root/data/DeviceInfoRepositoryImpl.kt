package com.waseem.libroom.feature.root.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.waseem.libroom.feature.root.device.DeviceInfo
import com.waseem.libroom.feature.root.domain.DeviceDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

object PreferencesKeys1 {
    val deviceInfo = intPreferencesKey("device_info")
}

class DeviceInfoRepositoryImpl @Inject constructor(
    private val preferenceDataStore: DataStore<Preferences>
) : DeviceDataRepository{

    override fun getDeviceState(): Flow<DeviceInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun setDeviceState(deviceInfo: DeviceInfo) {
        TODO("Not yet implemented")
    }
}
