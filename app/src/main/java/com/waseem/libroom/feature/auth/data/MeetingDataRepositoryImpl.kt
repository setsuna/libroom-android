package com.waseem.libroom.feature.auth.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.waseem.libroom.feature.auth.domain.Meeting
import com.waseem.libroom.feature.auth.domain.MeetingDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

object MeetingKeys {
    val MEETING_INFO = stringPreferencesKey("meeting_info")
}
class MeetingDataRepositoryImpl @Inject constructor(
    private val preferenceDataStore: DataStore<Preferences>
):MeetingDataRepository{
    override fun getMeetingInfo(): Flow<Meeting> {
        return preferenceDataStore.data.map { preferences ->
            val meetingJson = preferences[MeetingKeys.MEETING_INFO] ?: ""
            if (meetingJson.isNotEmpty()) {
                Json.decodeFromString<Meeting>(meetingJson)
            } else {
                Meeting(
                    httpFlvPlayPrefix = "",
                    isGuestUser = false,
                    isHistoryMeeting = false,
                    isHost = false,
                    isSignFree = null,
                    isSigned = null,
                    isSrsDisabled = false,
                    meeting = "",
                    meetingCreateDate = "",
                    mid = 0,
                    muid = 0,
                    name = "",
                    room = "",
                    roomId = 0,
                    rooms = null,
                    rtmpPushPrefix = "",
                    signType = null,
                    time = "",
                    token = "",
                    webrtcPlayPrefix = ""
                )
            }
        }
    }
    override suspend fun setMeetingInfo(meeting: Meeting) {
        preferenceDataStore.edit { preferences ->
            val meetingJson = Json.encodeToString(meeting)
            preferences[MeetingKeys.MEETING_INFO] = meetingJson
        }
    }
}