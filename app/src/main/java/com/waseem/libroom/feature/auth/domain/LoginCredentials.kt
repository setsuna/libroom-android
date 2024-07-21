package com.waseem.libroom.feature.auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentials(val email: String, val password: String)