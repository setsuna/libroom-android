package com.waseem.libroom.feature.meeting.domain

import kotlinx.serialization.Serializable

@Serializable
data class MeetingAgenda (
    val meetingTitle: String,
    val agendaList: List<AgendaItem>
)