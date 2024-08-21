package com.waseem.libroom.feature.meeting.domain

import kotlinx.coroutines.flow.Flow

interface MeetingAgendaRepository {

    suspend fun getAgendaItem(meetingId:String):Result<List<AgendaItem>>

    suspend fun getDocument(agendaId:String):Result<List<Document>>

    suspend fun getMeetingContent():Result<Flow<MeetingAgenda>>
}