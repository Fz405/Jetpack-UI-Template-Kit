package com.template.composeuikit.ui.screens.auth.signup

sealed interface SignUpUiState {
    data object Loading : SignUpUiState

    data class Success(
        val name: String = "",
        val nameError: String? = null,
        val email: String = "",
        val emailError: String? = null,
        val password: String = "",
        val passwordError: String? = null,
        val confirmPassword: String = "",
        val confirmPasswordError: String? = null
    ) : SignUpUiState

    data class Error(val message: String) : SignUpUiState
}

sealed interface SignUpUiEvent {
    data class OnNameChanged(val name: String) : SignUpUiEvent
    data class OnEmailChanged(val email: String) : SignUpUiEvent
    data class OnPasswordChanged(val password: String) : SignUpUiEvent
    data class OnConfirmPasswordChanged(val password: String) : SignUpUiEvent
    data object OnSubmitClicked : SignUpUiEvent
    data object OnLoginClicked : SignUpUiEvent
}