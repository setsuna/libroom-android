package com.waseem.libroom.feature.meeting.presentation

import com.waseem.libroom.core.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MeetingViewModel @Inject constructor(
    //private val getMeetingContent: GetMeetingContent,
    reducer: MeetingReducer
) : BaseStateViewModel<MeetingAction, MeetingResult, MeetingEvent, MeetingState, MeetingReducer>(
    initialState = MeetingState.DefaultState,
    reducer = reducer
) {
    init {
        action(MeetingAction.Load)
    }

    override fun MeetingAction.process(): Flow<MeetingResult> {
        return when (this) {
            is MeetingAction.Load -> loadMeetingContent()
            is MeetingAction.OpenAgenda -> emitEvent(MeetingEvent.NavigateToAgenda(agendaId))
            is MeetingAction.OpenAttendeeList -> emitEvent(MeetingEvent.NavigateToAttendeeList(meetingId))
        }
    }

    private fun loadMeetingContent(): Flow<MeetingResult> {
        // Implement loading logic here
    }
}