package com.waseem.libroom.feature.auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class Meeting(
    val httpFlvPlayPrefix: String,
    val isGuestUser: Boolean,
    val isHistoryMeeting: Boolean,
    val isHost: Boolean,
    val isSignFree: Boolean?,
    val isSigned: Boolean?,
    val isSrsDisabled: Boolean,
    val meeting: String,
    val meetingCreateDate: String,
    val mid: Int,
    val muid: Int,
    val name: String,
    val room: String,
    val roomId: Int,
    val rooms: List<Room>?,
    val rtmpPushPrefix: String,
    val signType:String?,
    val time: String,
    val token: String,
    val webrtcPlayPrefix: String
)