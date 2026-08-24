package com.template.composeuikit.ui.screens.auth.login
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.template.composeuikit.R
import com.template.composeuikit.ui.components.PrimaryButton
import com.template.composeuikit.ui.components.PrimaryTextField

// 1. Stateful Wrapper (Decoupled from DI frameworks for CodeCanyon)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier, // MUST BE FIRST
    viewModel: LoginViewModel = viewModel(), // STANDARD FACTORY
    onNavigateToSignUp: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {},
    onNavigateToOtp: (String) -> Unit = {}

) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state) {
        if (state is LoginUiState.NavigateToOtp) {
            val email = (state as LoginUiState.NavigateToOtp).email
            onNavigateToOtp(email)
            viewModel.onEvent(LoginUiEvent.OnNavigationHandled)
        }
    }

    LoginStatelessScreen(
        modifier = modifier,
        state = state,
        onEvent = { event ->
            when (event) {
                is LoginUiEvent.OnSignUpClicked -> onNavigateToSignUp()
                is LoginUiEvent.OnForgotPasswordClicked -> onNavigateToForgotPassword()
                // Hook in actual success navigation here when implemented
                else -> viewModel.onEvent(event)
            }
        }
    )
}

// 2. Stateless UI
@Composable
private fun LoginStatelessScreen(
    modifier: Modifier = Modifier,
    state: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        when (state) {
            is LoginUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is LoginUiState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { onEvent(LoginUiEvent.OnEmailChanged("")) }) {
                        Text(stringResource(R.string.action_retry))
                    }
                }
            }
            is LoginUiState.Success -> {
                LoginContent(
                    modifier = Modifier.fillMaxSize(),
                    data = state,
                    onEvent = onEvent
                )
            }
            is LoginUiState.NavigateToOtp -> {
                CircularProgressIndicator()
            }
        }
    }
}

// 3. Sub-components
@Composable
private fun LoginContent(
    modifier: Modifier = Modifier,
    data: LoginUiState.Success,
    onEvent: (LoginUiEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .padding(24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.login_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.login_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryTextField(
            value = data.email,
            onValueChange = { onEvent(LoginUiEvent.OnEmailChanged(it)) },
            label = stringResource(R.string.label_email),
            leadingIcon = Icons.Default.Email,
            isError = data.emailError != null,
            errorMessage = data.emailError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryTextField(
            value = data.password,
            onValueChange = { onEvent(LoginUiEvent.OnPasswordChanged(it)) },
            label = stringResource(R.string.label_password),
            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            isError = data.passwordError != null,
            errorMessage = data.passwordError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                onEvent(LoginUiEvent.OnSubmitClicked)
            })
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(onClick = { onEvent(LoginUiEvent.OnForgotPasswordClicked) }) {
                Text(stringResource(R.string.action_forgot_password))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
        text = stringResource(R.string.action_login),
        enabled = data.email.isNotBlank() && data.password.isNotBlank(),
        onClick = {
            focusManager.clearFocus()
            onEvent(LoginUiEvent.OnSubmitClicked)
        }
    )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { onEvent(LoginUiEvent.OnSignUpClicked) }) {
            Text(stringResource(R.string.prompt_no_account))
        }
    }
}


// 4. Previews
@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview_Success() {
    MaterialTheme {
        LoginStatelessScreen(
            state = LoginUiState.Success(),
            onEvent = {}
        )
    }
}