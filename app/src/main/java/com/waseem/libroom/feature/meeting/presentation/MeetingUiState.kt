package com.waseem.libroom.feature.meeting.presentation

data class MeetingUiState(
    val title: String,
    val agendaItems: List<AgendaItemUiState>,
    val attendees: List<AttendeeUiState>
)

data class AgendaItemUiState(
    val id: String,
    val title: String,
    val attachmentUrl: String?
)

data class AttendeeUiState(
    val id: String,
    val name: String
)