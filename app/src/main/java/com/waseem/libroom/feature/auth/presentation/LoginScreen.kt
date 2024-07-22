package com.waseem.libroom.feature.auth.presentation

import android.widget.Toast
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waseem.libroom.R
import com.waseem.libroom.core.compose.CircledLogo
import com.waseem.libroom.core.compose.EditText
import com.waseem.libroom.core.compose.FilledNetworkButton
import com.waseem.libroom.core.mvi.collectState
import com.waseem.libroom.core.ui.ThemedPreview
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    gotoMain: () -> Unit
) {
    val state by viewModel.collectState()
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    var focusedItem by remember { mutableStateOf<FocusedItem?>(null) }
    when(state) {
        is LoginState.SuccessState -> gotoMain()
        is LoginState.ErrorState -> {
            Toast.makeText(LocalContext.current, (state as LoginState.ErrorState).msg, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }

    Scaffold { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            item { CircledLogo() }
            item { Spacer(modifier = Modifier.height(100.dp)) }
            item {
                Text(
                    text = stringResource(R.string.login_screen_title),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                )
            }
            item {
                LoginForm(
                    state = state,
                    emailFocusRequester = emailFocusRequester,
                    passwordFocusRequester = passwordFocusRequester,
                    onEmailFocused = { focusedItem = FocusedItem.Email },
                    onPasswordFocused = { focusedItem = FocusedItem.Password },
                    onLoginClick = { email, password ->
                        viewModel.action(LoginAction.SignInClick(email, password))
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },
                    onForgetPasswordClick = { /*TODO*/ }
                )
            }
        }
    }
    LaunchedEffect(focusedItem) {
        focusedItem?.let { item ->
            val index = when (item) {
                FocusedItem.Email -> 3
                FocusedItem.Password -> 3
            }
            listState.animateScrollToItem(index)

            // 延迟一小段时间以确保滚动完成
            delay(300)

            // 额外滚动以确保输入框在键盘上方
            val extraScrollHeight = with(density) { 200.dp.toPx() }.toInt()
            listState.scrollBy(extraScrollHeight.toFloat())
        }
    }
}
enum class FocusedItem { Email, Password }

@Composable
private fun LoginForm(
    state: LoginState,
    emailFocusRequester: FocusRequester,
    passwordFocusRequester: FocusRequester,
    onEmailFocused: () -> Unit,
    onPasswordFocused: () -> Unit,
    onLoginClick: (email: String, password: String) -> Unit,
    onForgetPasswordClick: () -> Unit
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.onboarding_padding))) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.section_title_margin_bottom)))
        EditText(
            text = email.value,
            hint = stringResource(R.string.hint_email),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            onValueChange = { newValue: String -> email.value = newValue },
            modifier = Modifier
                .focusRequester(emailFocusRequester)
                .onFocusChanged { if (it.isFocused) onEmailFocused() }
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.field_margin_bottom)))
        EditText(
            text = password.value,
            hint = stringResource(R.string.password),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            onValueChange = { newValue: String -> password.value = newValue },
            modifier = Modifier
                .focusRequester(passwordFocusRequester)
                .onFocusChanged { if (it.isFocused) onPasswordFocused() }
        )
        val text = when(state) {
            is LoginState.SuccessState -> stringResource(id = R.string.login_success)
            is LoginState.ErrorState -> stringResource(id = R.string.retry_login)
            else -> stringResource(id = R.string.login)
        }
        FilledNetworkButton(
            text = text,
            loading = state == LoginState.LoadingState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.vertical_screen_margin))
        ) {
            onLoginClick(email.value, password.value)
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.section_title_margin_top))) {
            TextButton(
                onClick = onForgetPasswordClick,
                modifier = Modifier.align(alignment = Alignment.CenterEnd)
            ) {
                Text(
                    text = stringResource(R.string.forgot_password)
                )
            }
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview() {
    ThemedPreview {
        LoginScreen(viewModel = hiltViewModel()) {}
    }
}