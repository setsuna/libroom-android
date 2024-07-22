package com.waseem.libroom.feature.auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val httpFlvPlayPrefix: String,
    val room: String,
    val roomId: Int,
    val rtmpPushPrefix: String,
    val webrtcPlayPrefix: String
)