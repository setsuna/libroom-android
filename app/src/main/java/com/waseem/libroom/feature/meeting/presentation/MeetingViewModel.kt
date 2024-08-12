package com.waseem.libroom.feature.meeting.presentation

import com.waseem.libroom.core.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class MeetingViewModel @Inject constructor(
    private val getMeetingContent: GetMeetingContent,
    reducer: MeetingReducer
) : BaseStateViewModel<MeetingAction, MeetingResult, MeetingEvent, MeetingState, MeetingReducer>(
    initialState = MeetingState.Loading,
    reducer = reducer
) {
    init {
        action(MeetingAction.Load)
    }

    override fun MeetingAction.process(): Flow<MeetingResult> {
        return when (this) {
            MeetingAction.Load -> loadMeetingContent()
            is MeetingAction.ToggleAgendaExpansion -> toggleAgendaExpansion(agendaId)
        }
    }
    private fun loadMeetingContent(): Flow<MeetingResult> {
        return flow {
            emit(MeetingResult.Loading)
            getMeetingContent(NoParams)
                .onSuccess { meetingContent ->
                    emit(MeetingResult.Content(meetingContent.toUiState()))
                }
                .onFailure {
                    emit(MeetingResult.Error)
                }
        }
    }
    private fun toggleAgendaExpansion(agendaId: String): Flow<MeetingResult> {
        return flow {
            val currentState = state.value
            if (currentState is MeetingState.Content) {
                val updatedAgendas = currentState.meetingUiState.agendas.map { agenda ->
                    if (agenda.id == agendaId) {
                        agenda.copy(isExpanded = !agenda.isExpanded)
                    } else {
                        agenda
                    }
                }
                val updatedUiState = currentState.meetingUiState.copy(agendas = updatedAgendas)
                emit(MeetingResult.Content(updatedUiState))
            }
        }
    }
}