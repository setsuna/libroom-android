package com.waseem.libroom.feature.meeting.domain

import kotlinx.serialization.Serializable

@Serializable
data class AgendaItem(
    val creator: String,
    val gmtCreate: String,
    val isEditable: Boolean,
    val isJoinMeeting: Boolean,
    val issuesId: Int,
    val remark: String,
    val review: String,
    val statusEnum: String,
    val title: String
)