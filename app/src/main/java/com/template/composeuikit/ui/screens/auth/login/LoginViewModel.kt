package com.template.composeuikit.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.composeuikit.utils.AuthValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Success())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnEmailChanged -> {
                updateSuccessState { it.copy(email = event.email, emailError = null) }
            }
            is LoginUiEvent.OnPasswordChanged -> {
                updateSuccessState { it.copy(password = event.password, passwordError = null) }
            }
            is LoginUiEvent.OnSubmitClicked -> submitLogin()
            is LoginUiEvent.OnForgotPasswordClicked -> { /* Handled in UI navigation */ }
            is LoginUiEvent.OnSignUpClicked -> { /* Handled in UI navigation */ }
            is LoginUiEvent.OnNavigationHandled -> {
                val currentState = _uiState.value
                if (currentState is LoginUiState.NavigateToOtp) {
                    _uiState.value = LoginUiState.Success(email = currentState.email)
                }
            }
        }
    }

    private fun submitLogin() {
        val currentState = _uiState.value
        if (currentState !is LoginUiState.Success) return

        val isEmailValid = AuthValidator.isValidEmail(currentState.email)
        val isPasswordValid = AuthValidator.isValidPassword(currentState.password)

        if (!isEmailValid || !isPasswordValid) {
            _uiState.update {
                currentState.copy(
                    emailError = if (!isEmailValid) "Invalid email format" else null,
                    passwordError = if (!isPasswordValid) "Password must be at least 6 characters" else null
                )
            }
            return
        }

        // Simulate network call
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            delay(1500) // Mock API delay
            // TODO: Route to success or show error based on actual authentication
            _uiState.value = LoginUiState.NavigateToOtp(currentState.email)
        }


    }

    private fun updateSuccessState(update: (LoginUiState.Success) -> LoginUiState.Success) {
        val currentState = _uiState.value
        if (currentState is LoginUiState.Success) {
            _uiState.value = update(currentState)
        }
    }
}