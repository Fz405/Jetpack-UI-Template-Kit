```text
---
name: compose-ui-engineering
description: Builds production-quality, framework-agnostic Jetpack Compose screens for Nova UI Kit. Use when scaffolding features, creating screen triads, wiring navigation, managing UDF state, or enforcing Clean Architecture and Material3 standards.
---

# Compose UI Engineering & Architecture

## Overview

Build enterprise-grade, localized, testable Jetpack Compose screens following strict Unidirectional Data Flow (UDF) and Clean Architecture principles. Every screen must adhere to the Class Triad pattern, isolate state management from UI presentation, and comply with zero-tolerance quality gates.

---

## Dynamic Macros & Commands

### 1. [Automated Feature Generation Command: /feature]
When executing a `/feature` command, execute these steps in a single response:
1. **Architectural Scaffolding:** Verify the existence of `core`, `navigation`, `ui/components`, `ui/screens`, `ui/theme`, and `utils`. Create any missing directories. Inject `ui/components/PrimaryTextField.kt` using **Template A** if it does not exist.
2. **Feature Domain Resolution:** Determine semantic category (`auth`, `social`, `finance`). Target path MUST strictly be `ui/screens/<domain>/<feature_folder>/`.
3. **Domain Classes Generation (CRITICAL):** Inside the feature folder, create the class triad copying **Template C** character-for-character. Check `utils/` and generate `[FeatureName]Validator.kt` using **Template B** if validation is relevant.
4. **Strings:** Insert new strings into `res/values/strings.xml`.
5. **NavGraph:** Append the new route to the Navigation Graph in `navigation`, instantiate the ViewModel, and wire it to the Screen.
6. **Pre-Flight Self-Correction (MANDATORY CHAIN OF THOUGHT):** 
   Before you output ANY Kotlin code, you MUST output a short text audit answering these exact questions:
   - "Audit 1: Did I add `modifier: Modifier = Modifier` as the FIRST parameter to EVERY single Composable (Wrapper, Stateless, and Content)?" -> (If no, fix it before outputting).
   - "Audit 2: Did I hardcode ANY strings in `contentDescription` or `Text()`?" -> (If yes, replace with `stringResource` before outputting).
   - "Audit 3: Did I use standard viewModel()?" -> (If no, fix it).
7. **Report:** Output a "📊 Feature Generation Report" detailing file paths and a "✅ Compliance Check Passed" block.

### 2. [Dynamic Macros: /ui]
When I type `/ui [ClassName] [Optional Components]`:
- If components provided (e.g., `/ui ECommerceHome searchBar, list`): Build the screen including those items via `/feature` steps.
- If NO components provided: Build a clean, empty screen layout via `/feature` steps.

### 3. [Mass Domain Scaffolding Command: /scaffold-domain]
When I type `/scaffold-domain [DomainName]`, generate a complete suite routed to `ui/screens/[DomainName]/`:
- `auth`: `Login`, `SignUp`, `ForgotPassword`, `Otp`, `Onboarding`, `MainScreenLayout.kt`.
- `ecommerce`: `HomeFeed`, `ProductDetails`, `ShoppingCart`, `Checkout`, `OrderHistory`.
- `finance`: `WalletDashboard`, `CardManagement`, `TransactionHistory`, `TransferMoney`.
- `social`: `UserProfile`, `Settings`, `NotificationCenter`, `Messaging`.
- `legal`: `GdprConsent`, `PrivacyTerms`, `Feedback`, `AboutApp` (No validator required).
- *Execution:* For every screen, create the Triad, update `strings.xml`, wire NavGraph, and generate domain Validator using Template B.

## Dependency & Version Management (Zero Guessing Policy)

1. **Do Not Hallucinate Versions:**
   - Never invent, bump, or guess version numbers for Kotlin, KSP, AGP, or Compose libraries.
   - Always inspect `gradle/libs.versions.toml` to use existing aliases and version references.

2. **Stop on Version Incompatibilities:**
   - If a requested feature or library requires a dependency/compiler plugin that is not present or conflicts with the existing Kotlin/KSP setup:
     - **STOP immediately.**
     - Do not forcefully generate broken Gradle files.
     - State the exact incompatibility and prompt the user for the approved version.

3. **Strict Version Coupling Awareness:**
   - Kotlin version and KSP version MUST match (`<kotlin_version>-<ksp_patch>`).
   - Java `sourceCompatibility`/`targetCompatibility` MUST match `jvmTarget`.

---

## Anti-Pattern Enforcement

| AI Default Pattern | Why It Is Rejected | Production-Grade Requirement |
|---|---|---|
| `Hardcoded DI | Breaks UI component reusability | UI Wrappers must use standard viewModel() or accept ViewModels as parameters. Avoid hiltViewModel() in core UI files` |
| Hardcoded UI text | Breaks multi-language localization | ALL strings wrapped in `stringResource(R.string.*)` |
| `modifier` at the end | Violates Compose positional API | `modifier: Modifier = Modifier` must be FIRST |
| `Scaffold` in Wrapper | Prevents stateless UI previews | `Scaffold` belongs strictly in stateless UI |

---

## Code Templates

### Template A: PrimaryTextField.kt
```text
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
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
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

### Template B: Dynamic Validator Object

```text
import android.util.Patterns

object [FeatureName]Validator {
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun isValidPassword(password: String): Boolean {
        return password.isNotBlank() && password.length >= 6
    }
    fun isNameValid(name: String): Boolean {
        return name.isNotBlank() && name.length >= 2
    }
}

```

### Template C: The Class Triad Skeleton

Always use this exact architectural skeleton.

**File 1: `[FeatureName]Models.kt**`

```text
package com.template.novauikit.ui.screens.[domain].[feature_folder]

sealed interface [FeatureName]UiState {
    data object Loading : [FeatureName]UiState
    data class Success(
        // TODO: AI to inject domain-specific state variables here
        val exampleData: String = ""
    ) : [FeatureName]UiState
    data class Error(val message: String) : [FeatureName]UiState
}

sealed interface [FeatureName]UiEvent {
    // TODO: AI to inject domain-specific events here
    data object OnSubmitClicked : [FeatureName]UiEvent
    data class OnInputChanged(val newValue: String) : [FeatureName]UiEvent
}

```

**File 2: `[FeatureName]ViewModel.kt**`

```text
package com.template.novauikit.ui.screens.[domain].[feature_folder]

import androidx.lifecycle.ViewModel
        import androidx.lifecycle.viewModelScope
        import kotlinx.coroutines.flow.MutableStateFlow
        import kotlinx.coroutines.flow.StateFlow
        import kotlinx.coroutines.flow.asStateFlow
        import kotlinx.coroutines.flow.update
        import kotlinx.coroutines.launch

class [FeatureName]ViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<[FeatureName]UiState>([FeatureName]UiState.Loading)
    val uiState: StateFlow<[FeatureName]UiState> = _uiState.asStateFlow()

    init { loadInitialData() }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = [FeatureName]UiState.Success()
        }
    }

    fun onEvent(event: [FeatureName]UiEvent) {
    when (event) {
        is [FeatureName]UiEvent.OnInputChanged -> { /* TODO */ }
        is [FeatureName]UiEvent.OnSubmitClicked -> { /* TODO */ }
    }
}
}

```

```text
**File 3: `[FeatureName]Screen.kt**`

```kotlin
package com.template.novauikit.ui.screens.[domain].[feature_folder]

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

// 1. Stateful Wrapper
@Composable
fun [FeatureName]Screen(
    modifier: Modifier = Modifier, // MUST BE FIRST
    viewModel: [FeatureName]ViewModel = viewModel(), // STANDARD FACTORY
    onNavigateUp: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    [FeatureName]StatelessScreen(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier // MUST BE FIRST
    )
}

// 2. Stateless UI
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
                // 🛑 CRITICAL RULES: NO HARDCODED STRINGS (use stringResource). modifier FIRST.
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Build UI Here")
                }
            }
        }
    }
}

// 3. Previews
@Preview(showBackground = true)
@Composable
private fun [FeatureName]ScreenPreview_Success() {
    MaterialTheme {
        [FeatureName]StatelessScreen(state = [FeatureName]UiState.Success(), onEvent = {})
    }
}

// 4. Sub-components (STRICT PATTERN TO COPY)
@Composable
private fun ExampleSubComponent(
    modifier: Modifier = Modifier, // MANDATORY FIRST PARAMETER
    text: String
) {
    Box(modifier = modifier) { Text(text = text) }
}
```

---

## Verification & Pre-Flight Checklist

Before finishing output, verify:

* [ ] Directory is `ui/screens/<domain>/<feature_folder>/`
* [ ] Wrapper uses standard = viewModel()
* [ ] Zero hardcoded strings (`stringResource` used)
* [ ] ALL composables have `modifier: Modifier = Modifier` FIRST
* [ ] `NavGraph.kt` updated and outputted

```

