package com.waseem.libroom.core.usecase

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T> (
    val code: Int,
    val message: String,
    val data: T?
)

inline fun <reified T> ApiResponse<T>.toResult(): Result<T> {
    return when (code) {
        0 -> data?.let { Result.success(it) }
            ?: Result.failure(Exception("数据为空"))
        else -> Result.failure(Exception(message))
    }
}