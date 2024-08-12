package com.waseem.libroom.feature.meeting.domain

data class Meeting(
    val title: String,
    val agendaItems: List<AgendaItem>
)

data class AgendaItem(
    val id: String,
    val title: String,
    val documents: List<Document>
)

data class Document(
    val id: String,
    val name: String,
    val type: DocumentType
)

enum class DocumentType {
    PDF, DOCX, PPTX, UNKNOWN
}