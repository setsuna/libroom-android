package com.waseem.libroom.feature.auth.domain

import com.waseem.libroom.core.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface SignInWithPassword: UseCase<List<Meeting>,SignInWithPassword.Params> {
    data class Params( val account: String, val password: String)
}

class SignInWithPasswordImpl(
    private val dispatcher: CoroutineDispatcher,
    private val authWithPWDRepository: AuthWithPWDRepository
):SignInWithPassword{
    override suspend fun invoke(params: SignInWithPassword.Params): Result<List<Meeting>> {
        return withContext(dispatcher) {
            authWithPWDRepository.signInPWD(params.account,params.password)
        }
    }

}