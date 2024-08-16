package com.waseem.libroom.feature.meeting.domain

import com.waseem.libroom.core.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface GetAgendaItem: UseCase<List<AgendaItem>, GetAgendaItem.Params> {
    data class Params( val meetingId: String)
}

class GetAgendaItemImpl(
    private val dispatcher: CoroutineDispatcher,
    private val meetingAgendaRepository: MeetingAgendaRepository
):GetAgendaItem{
    override suspend fun invoke(params: GetAgendaItem.Params): Result<List<AgendaItem>> {
        return withContext(dispatcher) {
            meetingAgendaRepository.getAgendaItem(params.meetingId)
        }
    }
}