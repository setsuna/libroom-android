package com.waseem.libroom.feature.meeting.presentation

import com.waseem.libroom.core.mvi.MviAction
import com.waseem.libroom.core.mvi.MviEvent
import com.waseem.libroom.core.mvi.MviResult
import com.waseem.libroom.core.mvi.MviStateReducer
import com.waseem.libroom.core.mvi.MviViewState
import javax.inject.Inject

sealed class MeetingAction : MviAction {
    object Load : MeetingAction()
    data class ToggleAgendaExpand(val agendaId: String) : MeetingAction()
    data class LoadDocuments(val agendaId: String) : MeetingAction()
    data class OpenDocument(val documentId: String) : MeetingAction()
}

sealed class MeetingResult : MviResult {
    object Loading : MeetingResult()
    data class MeetingContent(val meetingUiState: MeetingUiState) : MeetingResult()
    data class DocumentsLoaded(val agendaId: String, val documents: List<DocumentUiState>) : MeetingResult()
    object Failure : MeetingResult()
}

sealed class MeetingEvent : MviEvent {
    data class OpenDocument(val documentId: String) : MeetingEvent()
}

sealed class MeetingState : MviViewState {
    object DefaultState : MeetingState()
    object LoadingState : MeetingState()
    data class MeetingContentState(val uiState: MeetingUiState) : MeetingState()
    object ErrorState : MeetingState()
}

class MeetingReducer @Inject constructor() : MviStateReducer<MeetingState, MeetingResult> {
    override fun MeetingState.reduce(result: MeetingResult): MeetingState {
        return when (val previousState = this) {
            is MeetingState.DefaultState -> previousState + result
            is MeetingState.ErrorState -> previousState + result
            is MeetingState.MeetingContentState -> previousState + result
            is MeetingState.LoadingState -> previousState + result
        }
    }

    private operator fun MeetingState.DefaultState.plus(result: MeetingResult): MeetingState {
        return when (result) {
            MeetingResult.Loading -> MeetingState.LoadingState
            else -> throw IllegalStateException("unsupported")
        }
    }

    private operator fun MeetingState.LoadingState.plus(result: MeetingResult): MeetingState {
        return when (result) {
            MeetingResult.Loading -> MeetingState.LoadingState
            is MeetingResult.MeetingContent -> MeetingState.MeetingContentState(uiState = result.meetingUiState)
            MeetingResult.Failure -> MeetingState.ErrorState
            else -> throw IllegalStateException("unsupported")
        }
    }

    private operator fun MeetingState.ErrorState.plus(result: MeetingResult): MeetingState {
        return when (result) {
            MeetingResult.Loading -> MeetingState.LoadingState
            else -> throw IllegalStateException("unsupported")
        }
    }

    private operator fun MeetingState.MeetingContentState.plus(result: MeetingResult): MeetingState {
        return when (result) {
            MeetingResult.Loading -> MeetingState.LoadingState
            is MeetingResult.MeetingContent -> MeetingState.MeetingContentState(uiState = result.meetingUiState)
            is MeetingResult.DocumentsLoaded -> {
                val updatedAgendaItems = this.uiState.agendaItems.map { agendaItem ->
                    /*if (agendaItem.id == result.agendaId) {
                        agendaItem.copy(documents = result.documents, isLoading = false)
                    } else {
                        agendaItem
                    }*/
                    agendaItem
                }
                MeetingState.MeetingContentState(this.uiState.copy(agendaItems = updatedAgendaItems))
            }
            else -> throw IllegalStateException("unsupported")
        }
    }
}