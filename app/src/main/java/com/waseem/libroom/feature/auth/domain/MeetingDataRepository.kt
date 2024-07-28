package com.waseem.libroom.feature.auth.domain

import kotlinx.coroutines.flow.Flow

interface MeetingDataRepository{
    fun getMeetingInfo():Flow<Meeting>
    suspend fun setMeetingInfo(meeting: Meeting)
}