package com.waseem.libroom.feature.meeting.domain

interface MeetingAgendaRepository {

    suspend fun getAgendaItem(meetingId:String):Result<List<AgendaItem>>

    suspend fun getDocument(agendaId:String):Result<List<Document>>

    suspend fun getMeetingContent():Result<MeetingAgenda>
}