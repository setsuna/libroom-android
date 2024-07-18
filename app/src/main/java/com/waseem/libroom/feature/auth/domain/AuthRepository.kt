package com.waseem.libroom.feature.auth.domain

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signOut(): Result<Boolean>
}

interface AuthWithPWDRepository {
    suspend fun signIn(email: String, password: String): Result<MeetingUser>
    suspend fun signOut(): Result<Boolean>
}