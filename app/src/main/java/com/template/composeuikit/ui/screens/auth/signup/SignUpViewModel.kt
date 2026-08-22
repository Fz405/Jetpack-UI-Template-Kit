package com.template.composeuikit.ui.screens.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.composeuikit.utils.AuthValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Success())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.OnNameChanged -> updateSuccessState { it.copy(name = event.name, nameError = null) }
            is SignUpUiEvent.OnEmailChanged -> updateSuccessState { it.copy(email = event.email, emailError = null) }
            is SignUpUiEvent.OnPasswordChanged -> updateSuccessState { it.copy(password = event.password, passwordError = null) }
            is SignUpUiEvent.OnConfirmPasswordChanged -> updateSuccessState { it.copy(confirmPassword = event.password, confirmPasswordError = null) }
            is SignUpUiEvent.OnSubmitClicked -> submitSignUp()
            is SignUpUiEvent.OnLoginClicked -> { /* Handled in UI navigation */ }
        }
    }

    private fun submitSignUp() {
        val currentState = _uiState.value
        if (currentState !is SignUpUiState.Success) return

        val isNameValid = AuthValidator.isNameValid(currentState.name)
        val isEmailValid = AuthValidator.isValidEmail(currentState.email)
        val isPasswordValid = AuthValidator.isValidPassword(currentState.password)
        val doPasswordsMatch = currentState.password == currentState.confirmPassword

        if (!isNameValid || !isEmailValid || !isPasswordValid || !doPasswordsMatch) {
            _uiState.update {
                currentState.copy(
                    nameError = if (!isNameValid) "Name must be at least 2 characters" else null,
                    emailError = if (!isEmailValid) "Invalid email format" else null,
                    passwordError = if (!isPasswordValid) "Password must be at least 6 characters" else null,
                    confirmPasswordError = if (!doPasswordsMatch) "Passwords do not match" else null
                )
            }
            return
        }

        // Simulate network call
        viewModelScope.launch {
            _uiState.value = SignUpUiState.Loading
            delay(1500)
            // TODO: Route to success/OTP or show error
            _uiState.value = SignUpUiState.Error("Sign up failed (Mock Error)")
        }
    }

    private fun updateSuccessState(update: (SignUpUiState.Success) -> SignUpUiState.Success) {
        val currentState = _uiState.value
        if (currentState is SignUpUiState.Success) {
            _uiState.value = update(currentState)
        }
    }
}