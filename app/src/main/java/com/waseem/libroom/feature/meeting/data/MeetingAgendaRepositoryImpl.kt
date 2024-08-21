package com.waseem.libroom.feature.meeting.data

import com.waseem.libroom.core.usecase.ApiResponse
import com.waseem.libroom.core.usecase.ApiResponseList
import com.waseem.libroom.core.usecase.toResult
import com.waseem.libroom.feature.auth.domain.Meeting
import com.waseem.libroom.feature.auth.domain.MeetingDataRepository
import com.waseem.libroom.feature.meeting.domain.AgendaItem
import com.waseem.libroom.feature.meeting.domain.Document
import com.waseem.libroom.feature.meeting.domain.MeetingAgenda
import com.waseem.libroom.feature.meeting.domain.MeetingAgendaRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

class MeetingAgendaRepositoryImpl @Inject constructor(
    private val meetingDataRepository: MeetingDataRepository,
    private var httpClient: HttpClient
) : MeetingAgendaRepository {
    override suspend fun getAgendaItem(meetingId:String): Result<List<AgendaItem>> {
        return try {
            val response = httpClient.post("/api/meeting-service/issues?mid=${meetingId}") {
                contentType(ContentType.Application.Json)
            }
            // 先解析 code
            val jsonElement = Json.parseToJsonElement(response.bodyAsText())
            val code = jsonElement.jsonObject["code"]?.jsonPrimitive?.int

            when (code) {
                0 -> {
                    val responseBody = response.body<ApiResponseList<AgendaItem>>()
                    responseBody.toResult()
                }
                1 -> {
                    val singleResponse = response.body<ApiResponse<AgendaItem>>()
                    // 将单个对象转换为列表，然后使用 ApiResponseList 的 toResult 方法
                    ApiResponseList(singleResponse.code, listOfNotNull(singleResponse.data), singleResponse.message).toResult()
                }
                else -> Result.failure(Exception("意外的响应代码: $code"))
            }
        } catch (e: Exception) {
            println("xunyidi getAgendaItem password ${e.message}")
            Result.failure(Exception("网络请求失败: ${e.message}"))
        }
    }

    override suspend fun getDocument(agendaId: String): Result<List<Document>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMeetingContent(): Result<Flow<MeetingAgenda>> {
        return  try {
            val meetingFlow = getMeetingData().getOrThrow()

            Result.success(meetingFlow.map { meeting ->
                val agendaItems = getAgendaItem(meeting.mid.toString()).getOrThrow()
                MeetingAgenda(
                    meetingTitle = meeting.name,
                    agendaList = agendaItems
                )
            })
        } catch (e: Exception) {
            Result.failure(Exception("Failed to get meeting content: ${e.message}"))
        }
    }

    private suspend fun getMeetingData(): Result<Flow<Meeting>> {
        return meetingDataRepository.getMeetingInfo().let { Result.success(it) }
    }
}