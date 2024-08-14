package com.waseem.libroom.feature.meeting.presentation

import com.waseem.libroom.feature.auth.domain.Meeting
import com.waseem.libroom.feature.meeting.domain.AgendaItem
import com.waseem.libroom.feature.meeting.domain.Document
import com.waseem.libroom.feature.meeting.domain.MeetingAgenda

data class MeetingUiState(
    val meetingTitle: String,
    val agendaItems: List<AgendaItemUiState>
)

data class AgendaItemUiState(
    val id: Int,
    val title: String,
    val isExpanded: Boolean,
    val isLoading: Boolean,
    val documents: List<DocumentUiState>?
)

data class DocumentUiState(
    val id: Int,
    val name: String,
    val fileType: FileType
)

enum class FileType {
    PDF, PPTX, DOCX, UNKNOWN
}

// Extension function to convert domain model to UI state
fun MeetingAgenda.toUiState(): MeetingUiState = MeetingUiState(
    meetingTitle = meetingTitle,
    agendaItems = agendaList.map { it.toUiState() }
)

fun AgendaItem.toUiState(): AgendaItemUiState = AgendaItemUiState(
    id = issuesId,
    title = title,
    isExpanded = false,
    isLoading = false,
    documents = null  // Documents will be loaded later
)

fun Document.toUiState(): DocumentUiState = DocumentUiState(
    id = issueDocumentId,
    name = name,
    fileType = when (suffix) {
        "pdf" -> FileType.PDF
        "pptx" -> FileType.PPTX
        "docx" -> FileType.DOCX
        else -> FileType.UNKNOWN
    }
)