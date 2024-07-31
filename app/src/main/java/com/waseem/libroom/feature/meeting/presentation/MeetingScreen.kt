package com.waseem.libroom.feature.meeting.presentation

import androidx.compose.foundation.background
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
import com.waseem.libroom.core.mvi.collectEvents
import com.waseem.libroom.core.mvi.collectState

@Composable
fun MeetingScreen(
    viewModel: MeetingViewModel,
    onNavigateToAgenda: (String) -> Unit,
    onNavigateToAttendeeList: (String) -> Unit
) {
    viewModel.collectEvents { event ->
        when (event) {
            is MeetingEvent.NavigateToAgenda -> onNavigateToAgenda(event.agendaId)
            is MeetingEvent.NavigateToAttendeeList -> onNavigateToAttendeeList(event.meetingId)
        }
    }

    val state by viewModel.collectState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "第12次厅长办公会", style = MaterialTheme.typography.headlineMedium)

        when (state) {
            is MeetingState.DefaultState -> {}
            is MeetingState.LoadingState -> CircularProgressIndicator()
            is MeetingState.ErrorState -> Text("Error loading meeting content")
            is MeetingState.ContentState -> {
                val contentState = state as MeetingState.ContentState
                MeetingContent(
                    uiState = contentState.uiState,
                    onOpenAgenda = { viewModel.action(MeetingAction.OpenAgenda(it)) },
                    onOpenAttendeeList = { viewModel.action(MeetingAction.OpenAttendeeList(it)) }
                )
            }
        }
    }
}

@Composable
fun MeetingContent(
    uiState: MeetingUiState,
    onOpenAgenda: (String) -> Unit,
    onOpenAttendeeList: (String) -> Unit
) {
    // Implement UI for meeting content, agenda items, and attendee list
}