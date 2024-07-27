package com.waseem.libroom.core.usecase

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T> (
    val code: Int,
    val message: String,
    val data: T?
)
@Serializable
data class ApiResponseList<T>(
    val code: Int,
    val data: List<T>,
    val message: String
)

inline fun <reified T> ApiResponse<T>.toResult(): Result<T> {
    return when (code) {
        0 -> data?.let { Result.success(it) }
            ?: Result.failure(Exception("数据为空"))
        else -> Result.failure(Exception(message))
    }
}

inline fun <reified T> ApiResponseList<T>.toResult(): Result<List<T>> {
    return when (code) {
        0 -> if (data.isNotEmpty()) {
            Result.success(data)
        } else {
            Result.failure(Exception("列表为空"))
        }
        else -> Result.failure(Exception(message))
    }
}