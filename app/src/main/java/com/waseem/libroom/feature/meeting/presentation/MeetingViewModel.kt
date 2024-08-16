package com.waseem.libroom.feature.meeting.presentation

import com.waseem.libroom.core.BaseStateViewModel
import com.waseem.libroom.core.usecase.NoParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class MeetingViewModel @Inject constructor(
    private val getMeetingContent: GetMeetingContent,
    //private val getAgendaDocuments: GetAgendaDocuments,
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
            MeetingAction.Load -> loadMeetingContent()
            is MeetingAction.ToggleAgendaExpand -> toggleAgendaExpand(agendaId)
            is MeetingAction.LoadDocuments -> loadDocuments(agendaId)
            is MeetingAction.OpenDocument -> openDocument(documentId)
        }
    }
    private fun loadMeetingContent(): Flow<MeetingResult> = flow {
        emit(MeetingResult.Loading)
        getMeetingContent(NoParams).fold(
            onSuccess = { meetingContent ->
                emit(MeetingResult.MeetingContent(meetingContent.toUiState()))
            },
            onFailure = {
                emit(MeetingResult.Failure)
            }
        )
    }
    private fun toggleAgendaExpand(agendaId: String): Flow<MeetingResult> = flow {
        val currentState = state.value as? MeetingState.MeetingContentState ?: return@flow
        val updatedAgendaItems = currentState.uiState.agendaItems.map { item ->
            if (item.id == agendaId) {
                val newExpandedState = !item.isExpanded
                item.copy(
                    isExpanded = newExpandedState,
                    isLoading = newExpandedState && item.documents == null
                )
            } else {
                item
            }
        }
        val updatedUiState = currentState.uiState.copy(agendaItems = updatedAgendaItems)
        emit(MeetingResult.MeetingContent(updatedUiState))

        // If we're expanding and haven't loaded documents yet, load them
        val expandedItem = updatedAgendaItems.find { it.id == agendaId }
        if (expandedItem?.isExpanded == true && expandedItem.documents == null) {
            action(MeetingAction.LoadDocuments(agendaId))
        }
    }

    private fun loadDocuments(agendaId: String): Flow<MeetingResult> = flow {
        /*getAgendaDocuments(agendaId).fold(
            onSuccess = { documents ->
                emit(MeetingResult.DocumentsLoaded(agendaId, documents.map { it.toUiState() }))
            },
            onFailure = {
                emit(MeetingResult.Failure)
            }
        )*/
    }

    private fun openDocument(documentId: String): Flow<MeetingResult> = flow {
        emitEvent(MeetingEvent.OpenDocument(documentId))
    }
}