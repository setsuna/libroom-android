package com.waseem.libroom.feature.auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentials(val deviceToken: String,val account: String, val password: String)