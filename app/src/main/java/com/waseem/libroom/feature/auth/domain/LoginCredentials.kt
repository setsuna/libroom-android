package com.waseem.libroom.feature.auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentials(val token: String,val username: String, val password: String)