package com.waseem.libroom.feature.meeting.domain

import kotlinx.serialization.Serializable

@Serializable
data class Document(
    val documentId: Int,
    val isEditable: Boolean,
    val isFolder: Boolean,
    val isHasPersionModifies: Boolean,
    val isSelfUploaded: Boolean,
    val issueDocumentId: Int,
    val name: String,
    val nextPath: String,
    val pdfDocumentId: Int,
    val secret: String,
    val secretEnum: String,
    val secretIndex: Int,
    val size: String,
    val sort: Int,
    val suffix: String,
    val thumbnailUrl: String,
    val url: String,
    val view: String
)