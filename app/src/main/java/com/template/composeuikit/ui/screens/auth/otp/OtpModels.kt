package com.template.composeuikit.ui.screens.auth.otp

sealed interface OtpUiState {
    data object Loading : OtpUiState

    data class Success(
        val otpCode: String = "",
        val isError: Boolean = false,
        val errorMessage: String? = null
    ) : OtpUiState

    data class Error(val message: String) : OtpUiState
}

sealed interface OtpUiEvent {
    data class OnOtpChanged(val code: String) : OtpUiEvent
    data object OnVerifyClicked : OtpUiEvent
    data object OnResendClicked : OtpUiEvent
    data object OnBackClicked : OtpUiEvent
}