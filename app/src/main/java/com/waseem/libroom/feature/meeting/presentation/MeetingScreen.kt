package com.waseem.libroom.feature.meeting.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import com.waseem.libroom.core.compose.ErrorUi
import com.waseem.libroom.core.compose.ScreenTitle
import com.waseem.libroom.core.mvi.collectEvents
import com.waseem.libroom.core.mvi.collectState

@Composable
fun MeetingScreen(
    viewModel: MeetingViewModel,
    onOpenDocument: (documentId: String) -> Unit
) {
    viewModel.collectEvents { event ->
        when (event) {
            is MeetingEvent.OpenDocument -> onOpenDocument(event.documentId)
        }
    }

    val state by viewModel.collectState()

    Column(modifier = Modifier.fillMaxSize()) {
        ScreenTitle(title = "第12次厅长办公会")
        when (state) {
            MeetingState.DefaultState -> {}
            MeetingState.LoadingState -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            MeetingState.ErrorState -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ErrorUi {
                        viewModel.action(MeetingAction.Load)
                    }
                }
            }
            is MeetingState.MeetingContentState -> {
                MeetingContent(
                    uiState = (state as MeetingState.MeetingContentState).uiState,
                    onToggleAgendaExpand = { agendaId ->
                        viewModel.action(MeetingAction.ToggleAgendaExpand(agendaId))
                    },
                    onOpenDocument = { documentId ->
                        viewModel.action(MeetingAction.OpenDocument(documentId))
                    }
                )
            }
        }
    }
}

@Composable
fun MeetingContent(
    uiState: MeetingUiState,
    onToggleAgendaExpand: (String) -> Unit,
    onOpenDocument: (String) -> Unit
) {
    LazyColumn {
        items(uiState.agendaItems) { agendaItem ->
            AgendaItem(
                agendaItem = agendaItem,
                onToggleExpand = { onToggleAgendaExpand(agendaItem.id) },
                onOpenDocument = onOpenDocument
            )
        }
    }
}

@Composable
fun AgendaItem(
    agendaItem: AgendaItemUiState,
    onToggleExpand: () -> Unit,
    onOpenDocument: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggleExpand)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = agendaItem.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (agendaItem.isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (agendaItem.isExpanded) "Collapse" else "Expand"
            )
        }
        if (agendaItem.isExpanded) {
            agendaItem.documents.forEach { document ->
                DocumentItem(document = document, onOpenDocument = onOpenDocument)
            }
        }
    }
}

@Composable
fun DocumentItem(
    document: DocumentUiState,
    onOpenDocument: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenDocument(document.id) }
            .padding(horizontal = 32.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(
                id = when (document.fileType) {
                    FileType.PDF -> R.drawable.ic_pdf
                    FileType.PPTX -> R.drawable.ic_pptx
                    FileType.DOCX -> R.drawable.ic_docx
                }
            ),
            contentDescription = "File type icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = document.title,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}