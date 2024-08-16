package com.waseem.libroom.feature.meeting.di

import com.waseem.libroom.feature.meeting.data.MeetingAgendaRepositoryImpl
import com.waseem.libroom.feature.meeting.domain.GetAgendaItem
import com.waseem.libroom.feature.meeting.domain.GetAgendaItemImpl
import com.waseem.libroom.feature.meeting.domain.MeetingAgendaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher

@InstallIn(ViewModelComponent::class)
@Module
object MeetingModule {

    @Provides
    fun providesMeetingDataRepository(httpClient: HttpClient): MeetingAgendaRepository {
        return MeetingAgendaRepositoryImpl(httpClient)
    }

    @Provides
    fun provideGetAgendaItem(
        dispatcher: CoroutineDispatcher,
        meetingAgendaRepository: MeetingAgendaRepository
    ): GetAgendaItem {
        return GetAgendaItemImpl(dispatcher = dispatcher, meetingAgendaRepository = meetingAgendaRepository)
    }
}