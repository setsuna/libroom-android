package com.waseem.libroom.feature.auth.domain

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signOut(): Result<Boolean>
}

interface AuthWithPWDRepository {
    suspend fun signInPWD(username: String, password: String): Result<MeetingUser>
    suspend fun signOutPWD(): Result<Boolean>
}