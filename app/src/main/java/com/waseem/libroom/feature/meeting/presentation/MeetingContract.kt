package com.waseem.libroom.feature.meeting.presentation

import com.waseem.libroom.core.mvi.MviAction
import com.waseem.libroom.core.mvi.MviEvent
import com.waseem.libroom.core.mvi.MviResult
import com.waseem.libroom.core.mvi.MviStateReducer
import com.waseem.libroom.core.mvi.MviViewState
import javax.inject.Inject

sealed class MeetingAction : MviAction {
    object Load : MeetingAction()
    data class OpenAgenda(val agendaId: String) : MeetingAction()
    data class OpenAttendeeList(val meetingId: String) : MeetingAction()
}

sealed class MeetingResult : MviResult {
    object Loading : MeetingResult()
    data class MeetingContent(val meetingUiState: MeetingUiState) : MeetingResult()
    object Failure : MeetingResult()
}

sealed class MeetingEvent : MviEvent {
    data class NavigateToAgenda(val agendaId: String) : MeetingEvent()
    data class NavigateToAttendeeList(val meetingId: String) : MeetingEvent()
}

sealed class MeetingState : MviViewState {
    object DefaultState : MeetingState()
    object LoadingState : MeetingState()
    data class ContentState(val uiState: MeetingUiState) : MeetingState()
    object ErrorState : MeetingState()
}

class MeetingReducer @Inject constructor() : MviStateReducer<MeetingState, MeetingResult> {
    override fun MeetingState.reduce(result: MeetingResult): MeetingState {
        // Implement state transitions here

    }
}