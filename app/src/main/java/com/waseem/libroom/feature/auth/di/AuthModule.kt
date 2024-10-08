package com.waseem.libroom.feature.auth.di

import com.google.firebase.auth.FirebaseAuth
import com.waseem.libroom.feature.auth.data.AuthRepositoryImpl
import com.waseem.libroom.feature.auth.data.AuthWithPWDRepositoryImpl
import com.waseem.libroom.feature.auth.domain.AuthRepository
import com.waseem.libroom.feature.auth.domain.AuthWithPWDRepository
import com.waseem.libroom.feature.auth.domain.SignInWithEmailPassword
import com.waseem.libroom.feature.auth.domain.SignInWithEmailPasswordImpl
import com.waseem.libroom.feature.auth.domain.SignInWithPassword
import com.waseem.libroom.feature.auth.domain.SignInWithPasswordImpl
import com.waseem.libroom.feature.auth.domain.SignOut
import com.waseem.libroom.feature.auth.domain.SignOutImpl
import com.waseem.libroom.feature.auth.domain.SignOutPWD
import com.waseem.libroom.feature.auth.domain.SignOutPWDImpl
import com.waseem.libroom.feature.auth.domain.UpdateDeviceToken
import com.waseem.libroom.feature.auth.domain.UpdateDeviceTokenImpl
import com.waseem.libroom.feature.auth.presentation.LoginAction
import com.waseem.libroom.feature.root.device.GetDeviceInfo
import com.waseem.libroom.feature.root.device.UpdateDeviceInfo
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

    //账号密码登录
    @Provides
    fun provideAuthWithPWDRepository(httpClient: HttpClient,getDeviceInfo: GetDeviceInfo):AuthWithPWDRepository{
        return AuthWithPWDRepositoryImpl(httpClient,getDeviceInfo)
    }

    @Provides
    fun provideSignInWithPassword(
        dispatcher: CoroutineDispatcher,
        authWithPWDRepository: AuthWithPWDRepository
    ):SignInWithPassword {
        return SignInWithPasswordImpl(dispatcher = dispatcher,authWithPWDRepository = authWithPWDRepository)
    }

    @Provides
    fun provideSignOutPWD(
        authWithPWDRepository: AuthWithPWDRepository
    ):SignOutPWD = SignOutPWDImpl(authWithPWDRepository = authWithPWDRepository)

    @Provides
    fun provideUpdateDeviceToken(
        dispatcher: CoroutineDispatcher,
        authWithPWDRepository: AuthWithPWDRepository
    ):UpdateDeviceToken{
        return UpdateDeviceTokenImpl(dispatcher = dispatcher,authWithPWDRepository = authWithPWDRepository)
    }
}