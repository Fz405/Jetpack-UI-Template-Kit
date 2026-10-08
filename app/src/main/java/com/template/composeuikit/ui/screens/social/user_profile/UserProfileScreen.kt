package com.template.composeuikit.ui.screens.social.user_profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.template.composeuikit.R
import com.template.composeuikit.ui.components.PrimaryButton
import com.template.composeuikit.ui.components.PrimaryTextField

// 1. Stateful Wrapper
@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier, // MUST BE FIRST
    viewModel: UserProfileViewModel = viewModel(), // STANDARD FACTORY
    onNavigateUp: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    UserProfileStatelessScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateUp = onNavigateUp,
        modifier = modifier // MUST BE FIRST
    )
}

// 2. Stateless UI
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserProfileStatelessScreen(
    state: UserProfileUiState,
    onEvent: (UserProfileUiEvent) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.user_profile_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .safeDrawingPadding()
        ) {
            when (state) {
                is UserProfileUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UserProfileUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UserProfileUiState.Success -> {
                    UserProfileContent(
                        data = state,
                        onEvent = onEvent,
                        modifier = Modifier.fillMaxSize().padding(16.dp)
                    )
                    
                    if (state.message != null) {
                        AlertDialog(
                            onDismissRequest = { onEvent(UserProfileUiEvent.OnDismissMessage) },
                            confirmButton = {
                                TextButton(onClick = { onEvent(UserProfileUiEvent.OnDismissMessage) }) {
                                    Text("OK")
                                }
                            },
                            text = { Text(state.message) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserProfileContent(
    data: UserProfileUiState.Success,
    onEvent: (UserProfileUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PrimaryTextField(
            value = data.username,
            onValueChange = { onEvent(UserProfileUiEvent.OnUsernameChanged(it)) },
            label = stringResource(R.string.label_username),
            isError = data.usernameError != null,
            errorMessage = data.usernameError
        )

        PrimaryTextField(
            value = data.bio,
            onValueChange = { onEvent(UserProfileUiEvent.OnBioChanged(it)) },
            label = stringResource(R.string.label_bio)
        )

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            text = stringResource(R.string.action_save),
            enabled = !data.isSaving,
            onClick = { onEvent(UserProfileUiEvent.OnSaveClicked) }
        )
    }
}

// 3. Previews
@Preview(showBackground = true)
@Composable
private fun UserProfileScreenPreview_Success() {
    MaterialTheme {
        UserProfileStatelessScreen(
            state = UserProfileUiState.Success(
                username = "JohnDoe",
                bio = "Hello World"
            ),
            onEvent = {},
            onNavigateUp = {}
        )
    }
}
