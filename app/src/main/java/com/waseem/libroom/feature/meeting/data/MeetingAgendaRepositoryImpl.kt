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

    override suspend fun getMeetingContent(meetingId: String): Result<Flow<MeetingAgenda>> {
        return try {
            // 获取议程项目
            val agendaItemsResult = getAgendaItem(meetingId)

            if (agendaItemsResult.isSuccess) {
                val agendaItems = agendaItemsResult.getOrNull() ?: emptyList()

                // 获取会议信息流
                val meetingFlowResult = getMeetingTitle()

                if (meetingFlowResult.isSuccess) {
                    val meetingFlow = meetingFlowResult.getOrNull()
                    if (meetingFlow != null) {
                        // 将 Meeting 流转换为 MeetingAgenda 流
                        val meetingAgendaFlow = meetingFlow.map { meeting ->
                            MeetingAgenda(meeting., agendaItems)
                        }
                        Result.success(meetingAgendaFlow)
                    } else {
                        Result.failure(Exception("Meeting flow is null"))
                    }
                } else {
                    Result.failure(Exception("Failed to fetch meeting information"))
                }
            } else {
                Result.failure(Exception("Failed to fetch agenda items"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to fetch meeting content: ${e.message}"))
        }
    }

    private suspend fun getMeetingTitle(): Result<Flow<Meeting>> {
        // 这里应该实现获取会议标题的逻辑
        // 为了示例，我们返回一个模拟的标题
        val meeting : Flow<Meeting> = meetingDataRepository.getMeetingInfo()
        return Result.success(meeting)
    }
}