package com.template.composeuikit.ui.screens.auth.forgotpassword

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.template.composeuikit.R
import com.template.composeuikit.ui.components.PrimaryButton
import com.template.composeuikit.ui.components.PrimaryTextField

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: ForgotPasswordViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToOtp: (String) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state) {
        if (state is ForgotPasswordUiState.NavigateToOtp) {
            // 👈 FIX
            val email = (state as ForgotPasswordUiState.NavigateToOtp).email
            onNavigateToOtp(email)
            viewModel.onEvent(ForgotPasswordUiEvent.OnNavigationHandled)
        }
    }

    ForgotPasswordStatelessScreen(
        modifier = modifier,
        state = state,
        onEvent = { event ->
            when (event) {
                is ForgotPasswordUiEvent.OnBackClicked -> onNavigateBack()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForgotPasswordStatelessScreen(
    modifier: Modifier = Modifier,
    state: ForgotPasswordUiState,
    onEvent: (ForgotPasswordUiEvent) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { onEvent(ForgotPasswordUiEvent.OnBackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .safeDrawingPadding(),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is ForgotPasswordUiState.Loading -> CircularProgressIndicator()
                is ForgotPasswordUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { onEvent(ForgotPasswordUiEvent.OnEmailChanged("")) }) {
                            Text(stringResource(R.string.action_retry))
                        }
                    }
                }
                is ForgotPasswordUiState.Success -> {
                    ForgotPasswordContent(
                        modifier = Modifier.fillMaxSize(),
                        data = state,
                        onEvent = onEvent
                    )
                }
                is ForgotPasswordUiState.NavigateToOtp -> {
                    CircularProgressIndicator()

                }
            }
        }
    }
}

@Composable
private fun ForgotPasswordContent(
    modifier: Modifier = Modifier,
    data: ForgotPasswordUiState.Success,
    onEvent: (ForgotPasswordUiEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .widthIn(max = 500.dp)
            .padding(24.dp)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top // Push content up to allow space for keyboard
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.forgot_password_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.forgot_password_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryTextField(
        value = data.email,
        onValueChange = { onEvent(ForgotPasswordUiEvent.OnEmailChanged(it)) },
        label = stringResource(R.string.label_email),
        leadingIcon = androidx.compose.material.icons.Icons.Default.Email, // 👈 Just add this line!
        isError = data.emailError != null,
        errorMessage = data.emailError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
            onEvent(ForgotPasswordUiEvent.OnSubmitClicked)
        })
    )

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = stringResource(R.string.action_send_link),
            enabled = data.email.isNotBlank(),
            onClick = {
                focusManager.clearFocus()
                onEvent(ForgotPasswordUiEvent.OnSubmitClicked)
            }
        )
           }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordPreview() {
    MaterialTheme {
        ForgotPasswordStatelessScreen(
            state = ForgotPasswordUiState.Success(),
            onEvent = {}
        )
    }
}