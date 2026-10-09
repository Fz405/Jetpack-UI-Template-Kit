---
name: compose-code-templates
description: Standardized production code templates for Class Triad, Primary Components, and Unit Tests.
---

# Compose Code Templates

## Template A: PrimaryTextField.kt

```kotlin
package com.template.novauikit.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = leadingIcon?.let { { Icon(imageVector = it, contentDescription = null) } },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            isError = isError,
            singleLine = true,
            enabled = enabled,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
```

## Template B: Feature Models (`[FeatureName]Models.kt`)

```kotlin
package com.template.novauikit.ui.screens.[domain].[feature_folder]

sealed interface [FeatureName]UiState {
    data object Loading : [FeatureName]UiState
    data class Success(
        val data: String = ""
    ) : [FeatureName]UiState
    data class Error(val message: String) : [FeatureName]UiState
}

sealed interface [FeatureName]UiEvent {
    data object OnSubmitClicked : [FeatureName]UiEvent
    data class OnInputChanged(val newValue: String) : [FeatureName]UiEvent
}

sealed interface [FeatureName]UiEffect {
    data object NavigateBack : [FeatureName]UiEffect
    data class ShowToast(val message: String) : [FeatureName]UiEffect
}
```

## Template C: ViewModel (`[FeatureName]ViewModel.kt`)

```kotlin
package com.template.novauikit.ui.screens.[domain].[feature_folder]

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class [FeatureName]ViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<[FeatureName]UiState>([FeatureName]UiState.Loading)
    val uiState: StateFlow<[FeatureName]UiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<[FeatureName]UiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init { loadInitialData() }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = [FeatureName]UiState.Success()
        }
    }

    fun onEvent(event: [FeatureName]UiEvent) {
        when (event) {
            is [FeatureName]UiEvent.OnInputChanged -> { /* Handle Input */ }
            is [FeatureName]UiEvent.OnSubmitClicked -> {
                viewModelScope.launch {
                    _uiEffect.send([FeatureName]UiEffect.ShowToast("Submitted Successfully"))
                }
            }
        }
    }
}
```

## Template D: Screen (`[FeatureName]Screen.kt`)

```kotlin
package com.template.novauikit.ui.screens.[domain].[feature_folder]

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun [FeatureName]Screen(
    modifier: Modifier = Modifier,
    viewModel: [FeatureName]ViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is [FeatureName]UiEffect.NavigateBack -> onNavigateUp()
                is [FeatureName]UiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    [FeatureName]StatelessScreen(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
private fun [FeatureName]StatelessScreen(
    state: [FeatureName]UiState,
    onEvent: ([FeatureName]UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        when (state) {
            is [FeatureName]UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is [FeatureName]UiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is [FeatureName]UiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Feature Content")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun [FeatureName]ScreenPreview() {
    MaterialTheme {
        [FeatureName]StatelessScreen(
            state = [FeatureName]UiState.Success(),
            onEvent = {}
        )
    }
}
```

## Template E: Unit Test (`[FeatureName]ViewModelTest.kt`)

```kotlin
package com.template.novauikit.ui.screens.[domain].[feature_folder]

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class [FeatureName]ViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: [FeatureName]ViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = [FeatureName]ViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initially emits Loading state followed by Success state`() = runTest {
        viewModel.uiState.test {
            assertEquals([FeatureName]UiState.Loading, awaitItem())
            testScheduler.advanceUntilIdle()
            assertEquals([FeatureName]UiState.Success(), awaitItem())
        }
    }

    @Test
    fun `OnSubmitClicked triggers ShowToast effect`() = runTest {
        viewModel.uiEffect.test {
            viewModel.onEvent([FeatureName]UiEvent.OnSubmitClicked)
            val effect = awaitItem()
            assertEquals([FeatureName]UiEffect.ShowToast("Submitted Successfully"), effect)
        }
    }
}
```