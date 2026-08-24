package com.template.composeuikit.ui.screens.auth.forgotpassword

import com.template.composeuikit.ui.screens.auth.login.LoginUiEvent
import com.template.composeuikit.ui.screens.auth.login.LoginUiState

sealed interface ForgotPasswordUiState {
    data object Loading : ForgotPasswordUiState

    data class Success(
        val email: String = "",
        val emailError: String? = null
    ) : ForgotPasswordUiState

    data class Error(val message: String) : ForgotPasswordUiState
    data class NavigateToOtp(val email: String) : ForgotPasswordUiState
}

sealed interface ForgotPasswordUiEvent {
    data class OnEmailChanged(val email: String) : ForgotPasswordUiEvent
    data object OnSubmitClicked : ForgotPasswordUiEvent
    data object OnBackClicked : ForgotPasswordUiEvent
    data object OnNavigationHandled : ForgotPasswordUiEvent
}