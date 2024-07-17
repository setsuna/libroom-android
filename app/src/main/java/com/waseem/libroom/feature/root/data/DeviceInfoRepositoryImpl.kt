package com.waseem.libroom.feature.root.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.waseem.libroom.feature.root.device.DeviceInfo
import com.waseem.libroom.feature.root.device.DeviceDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

object DeviceInfoKeys {
    val DEVICE_ID = intPreferencesKey("device_id")
    val DEVICE_TOKEN = stringPreferencesKey("device_token")
}

class DeviceInfoRepositoryImpl @Inject constructor(
    private val preferenceDataStore: DataStore<Preferences>
) : DeviceDataRepository {
    override fun getDeviceInfo(): Flow<DeviceInfo> = preferenceDataStore.data.map { preferences ->
        val deviceId = preferences[DeviceInfoKeys.DEVICE_ID]?:0
        val token = preferences[DeviceInfoKeys.DEVICE_TOKEN]?:""
        DeviceInfo(deviceId, token)
    }

    override suspend fun setDeviceInfo(deviceInfo: DeviceInfo) {
        preferenceDataStore.edit { preferences ->
            preferences[DeviceInfoKeys.DEVICE_ID] = deviceInfo.deviceId
            preferences[DeviceInfoKeys.DEVICE_TOKEN] = deviceInfo.token
        }
    }
}
