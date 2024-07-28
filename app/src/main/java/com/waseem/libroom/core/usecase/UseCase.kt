package com.waseem.libroom.core.usecase


interface UseCase<out T : Any, in Params : Any> {
    suspend operator fun invoke(params: Params): Result<T>
}
