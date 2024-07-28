package com.waseem.libroom.feature.auth.domain

import com.waseem.libroom.core.usecase.ObservableUseCase
import com.waseem.libroom.core.usecase.UseCase
import kotlinx.coroutines.flow.Flow

interface UpdateMeetingInfo: UseCase<Unit, UpdateMeetingInfo.Params> {
    data class Params(val meeting: Meeting)
}

class UpdateMeetingInfoImpl(
    private val meetingRepository: MeetingDataRepository
):UpdateMeetingInfo{
    override suspend fun invoke(params: UpdateMeetingInfo.Params): Result<Unit> {
        return Result.success(meetingRepository.setMeetingInfo(params.meeting))
    }
}