package com.waseem.libroom.feature.meeting.presentation

import com.waseem.libroom.core.mvi.MviAction
import com.waseem.libroom.core.mvi.MviEvent
import com.waseem.libroom.core.mvi.MviResult
import com.waseem.libroom.core.mvi.MviStateReducer
import com.waseem.libroom.core.mvi.MviViewState
import com.waseem.libroom.feature.meeting.domain.Meeting
import javax.inject.Inject

sealed class MeetingAction : MviAction {
    object Load : MeetingAction()
    data class ToggleAgendaExpansion(val id: String) : MeetingAction()
    //data class OpenDocument(val documentId: String) : MeetingAction()
}

sealed class MeetingResult : MviResult {
    object Loading : MeetingResult()
    data class Content(val meetingUiState: MeetingUiState) : MeetingResult()
    object Error : MeetingResult()
}

sealed class MeetingEvent : MviEvent {
    data class NavigateToAgenda(val agendaId: String) : MeetingEvent()
    data class NavigateToAttendeeList(val meetingId: String) : MeetingEvent()
    data class OpenDocument(val documentId: String) : MeetingEvent()
}

sealed class MeetingState : MviViewState {
    object Loading : MeetingState()
    data class Content(val meetingUiState: MeetingUiState) : MeetingState()
    object Error : MeetingState()
}

class MeetingReducer @Inject constructor() : MviStateReducer<MeetingState, MeetingResult> {
    override fun MeetingState.reduce(result: MeetingResult): MeetingState {
        return when (val currentState = this) {
            is MeetingState.Loading -> reduceFromLoading(result)
            is MeetingState.Content -> reduceFromContent(currentState, result)
            is MeetingState.Error -> reduceFromError(result)
        }
    }

    private fun reduceFromLoading(result: MeetingResult): MeetingState {
        return when (result) {
            is MeetingResult.Loading -> MeetingState.Loading
            is MeetingResult.Content -> MeetingState.Content(result.meetingUiState)
            is MeetingResult.Error -> MeetingState.Error
        }
    }

    private fun reduceFromContent(currentState: MeetingState.Content, result: MeetingResult): MeetingState {
        return when (result) {
            is MeetingResult.Loading -> MeetingState.Loading
            is MeetingResult.Content -> MeetingState.Content(result.meetingUiState)
            is MeetingResult.Error -> MeetingState.Error
        }
    }

    private fun reduceFromError(result: MeetingResult): MeetingState {
        return when (result) {
            is MeetingResult.Loading -> MeetingState.Loading
            is MeetingResult.Content -> MeetingState.Content(result.meetingUiState)
            is MeetingResult.Error -> MeetingState.Error
        }
    }
}