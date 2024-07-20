package com.waseem.libroom.feature.auth.domain

import com.waseem.libroom.core.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface SignInWithPassword: UseCase<MeetingUser,SignInWithPassword.Params> {
    data class Params(val username: String, val password: String)
}

class SignInWithPasswordImpl(
    private val dispatcher: CoroutineDispatcher,
    private val authWithPWDRepository: AuthWithPWDRepository
):SignInWithPassword{
    override suspend fun invoke(params: SignInWithPassword.Params): Result<MeetingUser> {
        return withContext(dispatcher) {
            authWithPWDRepository.signInPWD(params.username,params.password)
        }
    }

}