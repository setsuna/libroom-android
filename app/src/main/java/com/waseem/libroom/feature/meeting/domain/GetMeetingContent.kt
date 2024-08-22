package com.waseem.libroom.feature.meeting.domain

import com.waseem.libroom.core.usecase.NoParams
import com.waseem.libroom.core.usecase.UseCase
import com.waseem.libroom.feature.home.domain.HomeContent

interface GetMeetingContent : UseCase<HomeContent, NoParams>
class GetMeetingContent {
}