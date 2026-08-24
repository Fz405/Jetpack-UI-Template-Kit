package com.template.composeuikit.ui.screens.auth.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.composeuikit.ui.screens.auth.login.LoginUiState
import com.template.composeuikit.utils.AuthValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ForgotPasswordUiState>(ForgotPasswordUiState.Success())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun onEvent(event: ForgotPasswordUiEvent) {
        when (event) {
            is ForgotPasswordUiEvent.OnEmailChanged -> {
                updateSuccessState { it.copy(email = event.email, emailError = null) }
            }
            is ForgotPasswordUiEvent.OnSubmitClicked -> submitResetRequest()
            is ForgotPasswordUiEvent.OnBackClicked -> { /* Handled in UI navigation */ }
            is ForgotPasswordUiEvent.OnNavigationHandled -> {
                val currentState = _uiState.value
                if (currentState is ForgotPasswordUiState.NavigateToOtp) {
                    _uiState.value = ForgotPasswordUiState.Success(email = currentState.email)
                }
            }
        }
    }

    private fun submitResetRequest() {
        val currentState = _uiState.value
        if (currentState !is ForgotPasswordUiState.Success) return

        val isEmailValid = AuthValidator.isValidEmail(currentState.email)

        if (!isEmailValid) {
            _uiState.update {
                currentState.copy(emailError = "Invalid email format")
            }
            return
        }

        // Simulate network call
        viewModelScope.launch {
            _uiState.value = ForgotPasswordUiState.Loading
            delay(1500)
            // TODO: Route to OTP or show success dialogue
            _uiState.value = ForgotPasswordUiState.NavigateToOtp(currentState.email)
        }
    }

    private fun updateSuccessState(update: (ForgotPasswordUiState.Success) -> ForgotPasswordUiState.Success) {
        val currentState = _uiState.value
        if (currentState is ForgotPasswordUiState.Success) {
            _uiState.value = update(currentState)
        }
    }
}