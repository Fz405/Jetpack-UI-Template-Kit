package com.template.composeuikit.ui.screens.auth.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OtpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<OtpUiState>(OtpUiState.Success())
    val uiState: StateFlow<OtpUiState> = _uiState.asStateFlow()

    // Enterprise Standard: Define exact OTP length
    private val otpLength = 6

    fun onEvent(event: OtpUiEvent) {
        when (event) {
            is OtpUiEvent.OnOtpChanged -> {
                if (event.code.length <= otpLength) {
                    updateSuccessState { it.copy(otpCode = event.code, isError = false, errorMessage = null) }
                }
            }
            is OtpUiEvent.OnVerifyClicked -> verifyOtp()
            is OtpUiEvent.OnResendClicked -> resendOtp()
            is OtpUiEvent.OnBackClicked -> { /* Handled in UI */ }
        }
    }

    private fun verifyOtp() {
        val currentState = _uiState.value
        if (currentState !is OtpUiState.Success) return

        if (currentState.otpCode.length < otpLength) {
            _uiState.update {
                currentState.copy(isError = true, errorMessage = "Please enter the complete 6-digit code")
            }
            return
        }

        viewModelScope.launch {
            _uiState.value = OtpUiState.Loading
            delay(1500) // Mock API Call
            // TODO: Navigate to Home screen on actual success
            _uiState.value = OtpUiState.Error("Invalid Code (Mock Error)")
        }
    }

    private fun resendOtp() {
        // Mock resend logic
        updateSuccessState { it.copy(otpCode = "", isError = false, errorMessage = null) }
    }

    private fun updateSuccessState(update: (OtpUiState.Success) -> OtpUiState.Success) {
        val currentState = _uiState.value
        if (currentState is OtpUiState.Success) {
            _uiState.value = update(currentState)
        }
    }
}