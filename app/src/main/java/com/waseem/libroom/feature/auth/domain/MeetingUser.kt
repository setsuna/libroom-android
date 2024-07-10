package com.waseem.libroom.feature.auth.domain

data class MeetingUser(
    val department: String,
    val isNeedResetPsw: Boolean,
    val name: String,
    val post: String,
    val token: String
)