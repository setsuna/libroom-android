package com.waseem.libroom.feature.meeting.presentation

import com.waseem.libroom.feature.auth.domain.Meeting
import com.waseem.libroom.feature.meeting.domain.AgendaItem
import com.waseem.libroom.feature.meeting.domain.Document
import com.waseem.libroom.feature.meeting.domain.DocumentUiState

data class MeetingUiState(
    val meetingTitle: String,
    val agendaItems: List<AgendaItemUiState>
)

data class AgendaItemUiState(
    val id: String,
    val title: String,
    val isExpanded: Boolean,
    val isLoading: Boolean,
    val documents: List<DocumentUiState>?
)

data class DocumentUiState(
    val id: String,
    val title: String,
    val fileType: FileType
)

enum class FileType {
    PDF, PPTX, DOCX, UNKNOWN
}

// Extension function to convert domain model to UI state
fun Meeting.toUiState(): MeetingUiState = MeetingUiState(
    meetingTitle = title,
    agendaItems = agendaItems.map { it.toUiState() }
)

fun AgendaItem.toUiState(): AgendaItemUiState = AgendaItemUiState(
    id = id,
    title = title,
    isExpanded = false,
    isLoading = false,
    documents = null  // Documents will be loaded later
)

fun Document.toUiState(): DocumentUiState = DocumentUiState(
    id = id,
    title = title,
    fileType = when (fileExtension.toLowerCase()) {
        "pdf" -> FileType.PDF
        "pptx" -> FileType.PPTX
        "docx" -> FileType.DOCX
        else -> throw IllegalArgumentException("Unsupported file type: $fileExtension")
    }
)