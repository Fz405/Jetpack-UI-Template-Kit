package com.template.composeuikit.ui.screens.auth.login
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.template.composeuikit.R
import com.template.composeuikit.ui.components.PrimaryTextField

// 1. Stateful Wrapper (Decoupled from DI frameworks for CodeCanyon)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier, // MUST BE FIRST
    viewModel: LoginViewModel = viewModel(), // STANDARD FACTORY
    onNavigateToSignUp: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LoginStatelessScreen(
        modifier = modifier,
        state = state,
        onEvent = { event ->
            when (event) {
                is LoginUiEvent.OnSignUpClicked -> onNavigateToSignUp()
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

        Spacer(modifier = Modifier.height(32.dp))

        // Using our custom component!
        PrimaryTextField(
            value = data.email,
            onValueChange = { onEvent(LoginUiEvent.OnEmailChanged(it)) },
            label = stringResource(R.string.label_email),
            isError = data.emailError != null,
            errorMessage = data.emailError
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryTextField(
            value = data.password,
            onValueChange = { onEvent(LoginUiEvent.OnPasswordChanged(it)) },
            label = stringResource(R.string.label_password),
            isPassword = true,
            isError = data.passwordError != null,
            errorMessage = data.passwordError
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onEvent(LoginUiEvent.OnSubmitClicked) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = stringResource(R.string.action_login),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

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