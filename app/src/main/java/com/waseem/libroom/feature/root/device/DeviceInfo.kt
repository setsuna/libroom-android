package com.waseem.libroom.feature.root.device

import kotlinx.serialization.Serializable

@Serializable
data class DeviceInfo (
    val deviceId: Int,
    val token: String
)