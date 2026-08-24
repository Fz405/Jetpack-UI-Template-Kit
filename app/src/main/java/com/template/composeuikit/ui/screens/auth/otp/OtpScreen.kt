package com.template.composeuikit.ui.screens.auth.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun OtpScreen(
    modifier: Modifier = Modifier,
    email: String, // Passed in from NavGraph
    viewModel: OtpViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    OtpStatelessScreen(
        modifier = modifier,
        email = email,
        state = state,
        onEvent = { event ->
            when (event) {
                is OtpUiEvent.OnBackClicked -> onNavigateBack()
                is OtpUiEvent.OnVerifyClicked -> {
                    viewModel.onEvent(event)
                    onNavigateToHome()
                }
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OtpStatelessScreen(
    modifier: Modifier = Modifier,
    email: String,
    state: OtpUiState,
    onEvent: (OtpUiEvent) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { onEvent(OtpUiEvent.OnBackClicked) }) {
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
                is OtpUiState.Loading -> CircularProgressIndicator()
                is OtpUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { onEvent(OtpUiEvent.OnOtpChanged("")) }) {
                            Text(stringResource(R.string.action_retry))
                        }
                    }
                }
                is OtpUiState.Success -> {
                    OtpContent(
                        modifier = Modifier.fillMaxSize(),
                        email = email,
                        data = state,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun OtpContent(
    modifier: Modifier = Modifier,
    email: String,
    data: OtpUiState.Success,
    onEvent: (OtpUiEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .widthIn(max = 500.dp)
            .padding(24.dp)
            .imePadding() // Pushes the UI up when the keyboard appears
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Center looks best when scrollable
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.otp_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.otp_subtitle, email), // Dynamic String!
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Premium Custom OTP Component
        OtpInputField(
            modifier = Modifier.fillMaxWidth(),
            otpText = data.otpCode,
            isError = data.isError,
            onOtpChanged = { onEvent(OtpUiEvent.OnOtpChanged(it)) }
        )

        if (data.isError && data.errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = data.errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        PrimaryButton(
            text = stringResource(R.string.action_verify),
            enabled = data.otpCode.length == 6,
            onClick = {
                focusManager.clearFocus()
                onEvent(OtpUiEvent.OnVerifyClicked)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.prompt_didnt_receive),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = { onEvent(OtpUiEvent.OnResendClicked) }) {
                Text(stringResource(R.string.action_resend))
            }
        }
    }
}

// 4. Premium Segmented OTP Input
@Composable
private fun OtpInputField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpLength: Int = 6,
    isError: Boolean = false,
    onOtpChanged: (String) -> Unit
) {
    BasicTextField(
        value = otpText,
        onValueChange = { if (it.length <= otpLength) onOtpChanged(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done),
        decorationBox = {
            Row(
                modifier = modifier.fillMaxWidth(), // Ensure the Row takes the full screen width
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                repeat(otpLength) { index ->
                    val char = when {
                        index >= otpText.length -> ""
                        else -> otpText[index].toString()
                    }
                    val isFocused = otpText.length == index
                    val borderColor = when {
                        isError -> MaterialTheme.colorScheme.error
                        isFocused -> MaterialTheme.colorScheme.primary
                        char.isNotEmpty() -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        else -> MaterialTheme.colorScheme.outlineVariant
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f) // 👈 THE RESPONSIVE MAGIC! It auto-scales width.
                            .aspectRatio(0.85f) // 👈 Keeps the boxes slightly taller than they are wide, no matter the screen size
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = if (isFocused || char.isNotEmpty()) 2.dp else 1.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}