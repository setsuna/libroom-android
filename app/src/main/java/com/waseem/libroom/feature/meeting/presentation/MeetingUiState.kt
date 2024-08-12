package com.waseem.libroom.feature.meeting.presentation

data class MeetingUiState(
    val title: String,
    val agendas: List<AgendaUiState>
)

data class AgendaUiState(
    val id: String,
    val title: String,
    val isExpanded: Boolean,
    val files: List<FileUiState>
)

data class FileUiState(
    val id: String,
    val name: String,
    val type: FileType
)

enum class FileType {
    PDF, PPTX, DOCX, UNKNOWN
}