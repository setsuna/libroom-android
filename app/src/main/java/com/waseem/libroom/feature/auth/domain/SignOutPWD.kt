package com.waseem.libroom.feature.auth.domain

import com.waseem.libroom.core.usecase.NoParams
import com.waseem.libroom.core.usecase.UseCase

interface SignOutPWD:UseCase<Boolean,NoParams>

class SignOutPWDImpl(
    private val authWithPWDRepository: AuthWithPWDRepository
):SignOutPWD{
    override suspend fun invoke(params: NoParams): Result<Boolean> {
        return authWithPWDRepository.signOutPWD()
    }

}