package com.waseem.libroom.feature.auth.domain

import com.waseem.libroom.core.usecase.NoParams
import com.waseem.libroom.core.usecase.ObservableUseCase
import kotlinx.coroutines.flow.Flow

interface GetMeetingInfo: ObservableUseCase<Meeting, NoParams>

class GetMeetingInfoImpl(
    private val meetingDataRepository: MeetingDataRepository
):GetMeetingInfo{
    override fun invoke(params: NoParams) = meetingDataRepository.getMeetingInfo()
}