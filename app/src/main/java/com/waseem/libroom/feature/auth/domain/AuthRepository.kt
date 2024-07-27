package com.waseem.libroom.feature.auth.domain

import com.waseem.libroom.feature.root.device.DeviceInfo

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signOut(): Result<Boolean>
}

interface AuthWithPWDRepository {
    suspend fun signInPWD(username: String, password: String): Result<List<Meeting>>
    suspend fun signOutPWD(): Result<Boolean>
    suspend fun updateDeviceToken(deviceType:String,deviceCode:String):Result<DeviceInfo>
}