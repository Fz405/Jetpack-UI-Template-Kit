package com.template.composeuikit.ui.screens.auth.login

sealed interface LoginUiState {
    data object Loading : LoginUiState

    // Success state holds the active form data
    data class Success(
        val email: String = "",
        val emailError: String? = null,
        val password: String = "",
        val passwordError: String? = null
    ) : LoginUiState

    data class Error(val message: String) : LoginUiState
}

sealed interface LoginUiEvent {
    data class OnEmailChanged(val email: String) : LoginUiEvent
    data class OnPasswordChanged(val password: String) : LoginUiEvent
    data object OnSubmitClicked : LoginUiEvent
    data object OnForgotPasswordClicked : LoginUiEvent
    data object OnSignUpClicked : LoginUiEvent
}