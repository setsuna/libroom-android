package com.waseem.libroom.feature.auth.di

import com.google.firebase.auth.FirebaseAuth
import com.waseem.libroom.feature.auth.data.AuthRepositoryImpl
import com.waseem.libroom.feature.auth.data.AuthWithPWDRepositoryImpl
import com.waseem.libroom.feature.auth.domain.AuthRepository
import com.waseem.libroom.feature.auth.domain.AuthWithPWDRepository
import com.waseem.libroom.feature.auth.domain.SignInWithEmailPassword
import com.waseem.libroom.feature.auth.domain.SignInWithEmailPasswordImpl
import com.waseem.libroom.feature.auth.domain.SignOut
import com.waseem.libroom.feature.auth.domain.SignOutImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher

@InstallIn(ViewModelComponent::class)
@Module
object AuthModule {

    @Provides
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth = firebaseAuth)
    }

    @Provides
    fun provideSignInWithEmailAndPassword(
        dispatcher: CoroutineDispatcher,
        authRepository: AuthRepository
    ): SignInWithEmailPassword {
        return SignInWithEmailPasswordImpl(dispatcher = dispatcher, authRepository = authRepository)
    }

    @Provides
    fun provideSignOut(
        authRepository: AuthRepository
    ): SignOut = SignOutImpl(authRepository = authRepository)

    @Provides
    fun provideApiAuthRepository(httpClient: HttpClient):AuthWithPWDRepository {
        return AuthWithPWDRepositoryImpl(httpClient)
    }
}