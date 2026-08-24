package com.template.composeuikit.ui.screens.auth.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.template.composeuikit.ui.components.PrimaryTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.LaunchedEffect
import com.template.composeuikit.ui.components.PrimaryButton

// 1. Stateful Wrapper
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel(),
    onNavigateToLogin: () -> Unit = {},
    onNavigateToOtp: (String) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state) {
        if (state is SignUpUiState.NavigateToOtp) {
            // 👈 FIX: Explicitly cast 'state' using (state as ...)
            val email = (state as SignUpUiState.NavigateToOtp).email
            onNavigateToOtp(email)
            viewModel.onEvent(SignUpUiEvent.OnNavigationHandled)
        }
    }

    SignUpStatelessScreen(
        modifier = modifier,
        state = state,
        onEvent = { event ->
            when (event) {
                is SignUpUiEvent.OnLoginClicked -> onNavigateToLogin()
                // TODO: Hook in onSuccess logic to trigger onNavigateToOtp(email)
                else -> viewModel.onEvent(event)
            }
        }
    )
}

// 2. Stateless UI
@Composable
private fun SignUpStatelessScreen(
    modifier: Modifier = Modifier,
    state: SignUpUiState,
    onEvent: (SignUpUiEvent) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is SignUpUiState.Loading -> {
                CircularProgressIndicator()
            }
            is SignUpUiState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { onEvent(SignUpUiEvent.OnNameChanged("")) }) {
                        Text(stringResource(R.string.action_retry))
                    }
                }
            }
            is SignUpUiState.Success -> {
                SignUpContent(
                    modifier = Modifier.fillMaxSize(),
                    data = state,
                    onEvent = onEvent
                )
            }
            is SignUpUiState.NavigateToOtp -> {
                CircularProgressIndicator()
            }
        }
    }
}

// 3. Sub-components
@Composable
private fun SignUpContent(
    modifier: Modifier = Modifier,
    data: SignUpUiState.Success,
    onEvent: (SignUpUiEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .widthIn(max = 500.dp)
            .padding(24.dp)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.signup_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.signup_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryTextField(
            value = data.name,
            onValueChange = { onEvent(SignUpUiEvent.OnNameChanged(it)) },
            label = stringResource(R.string.label_name),
            leadingIcon = Icons.Default.Person,
            isError = data.nameError != null,
            errorMessage = data.nameError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryTextField(
            value = data.email,
            onValueChange = { onEvent(SignUpUiEvent.OnEmailChanged(it)) },
            label = stringResource(R.string.label_email),
            leadingIcon = Icons.Default.Email,
            isError = data.emailError != null,
            errorMessage = data.emailError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryTextField(
            value = data.password,
            onValueChange = { onEvent(SignUpUiEvent.OnPasswordChanged(it)) },
            label = stringResource(R.string.label_password),
            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            isError = data.passwordError != null,
            errorMessage = data.passwordError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryTextField(
            value = data.confirmPassword,
            onValueChange = { onEvent(SignUpUiEvent.OnConfirmPasswordChanged(it)) },
            label = stringResource(R.string.label_confirm_password),
            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            isError = data.confirmPasswordError != null,
            errorMessage = data.confirmPasswordError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                onEvent(SignUpUiEvent.OnSubmitClicked)
            })
        )

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = stringResource(R.string.action_signup),
            enabled = data.name.isNotBlank() && data.email.isNotBlank() &&
                    data.password.isNotBlank() && data.confirmPassword.isNotBlank(),
            onClick = {
                focusManager.clearFocus()
                onEvent(SignUpUiEvent.OnSubmitClicked)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { onEvent(SignUpUiEvent.OnLoginClicked) }) {
            Text(stringResource(R.string.prompt_have_account))
        }
    }
}


// 4. Previews
@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview_Success() {
    MaterialTheme {
        SignUpStatelessScreen(
            state = SignUpUiState.Success(),
            onEvent = {}
        )
    }
}