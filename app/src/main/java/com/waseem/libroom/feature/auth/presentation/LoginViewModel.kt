package com.waseem.libroom.feature.auth.presentation

import com.waseem.libroom.core.BaseStateViewModel
import com.waseem.libroom.feature.auth.domain.MeetingDataRepository
import com.waseem.libroom.feature.auth.domain.SignInWithPassword
import com.waseem.libroom.feature.auth.domain.UpdateDeviceToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInWithPassword: SignInWithPassword,
    private val meetingDataRepository: MeetingDataRepository,
    reducer: LoginReducer
) : BaseStateViewModel<LoginAction, LoginResult, LoginEvent, LoginState, LoginReducer>(
    initialState = LoginState.DefaultState,
    reducer = reducer
){
    override fun LoginAction.process(): Flow<LoginResult> {
        return when(this) {
            is LoginAction.SignInClick -> {
                flow {
                    signInWithPassword(
                        params = SignInWithPassword.Params(
                            account = email,
                            password = password
                        )
                    ).onSuccess {meetings ->
                        meetings.firstOrNull()?.let { meeting ->
                            meetingDataRepository.setMeetingInfo(meeting)
                        }
                        emit(LoginResult.Success)
                    }.onFailure {
                        emit(LoginResult.Failure(msg = it.message ?: "Something went wrong"))
                    }
                }.onStart {
                    emit(LoginResult.Loading)
                }.catch {
                    emit(LoginResult.Failure(msg = it.message ?: "Something went wrong"))
                }
            }
        }
    }
}