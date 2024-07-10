package com.waseem.libroom.feature.root.domain

import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    fun getAuthState(): Flow<AuthState>

    suspend fun setAuthState(authState: AuthState)
}