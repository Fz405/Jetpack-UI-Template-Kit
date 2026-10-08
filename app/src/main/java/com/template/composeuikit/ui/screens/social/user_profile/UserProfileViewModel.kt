package com.template.composeuikit.ui.screens.social.user_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.composeuikit.utils.UserProfileValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UserProfileUiState>(UserProfileUiState.Loading)
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            delay(1000) // Mock API Call
            _uiState.value = UserProfileUiState.Success(
                username = "JohnDoe",
                bio = "Android Developer & Compose Enthusiast"
            )
        }
    }

    fun onEvent(event: UserProfileUiEvent) {
        when (event) {
            is UserProfileUiEvent.OnUsernameChanged -> {
                updateSuccessState { it.copy(username = event.newValue, usernameError = null) }
            }
            is UserProfileUiEvent.OnBioChanged -> {
                updateSuccessState { it.copy(bio = event.newValue) }
            }
            is UserProfileUiEvent.OnSaveClicked -> saveProfile()
            is UserProfileUiEvent.OnDismissMessage -> {
                updateSuccessState { it.copy(message = null) }
            }
        }
    }

    private fun saveProfile() {
        val currentState = _uiState.value
        if (currentState !is UserProfileUiState.Success) return

        if (!UserProfileValidator.isUsernameValid(currentState.username)) {
            _uiState.update { 
                currentState.copy(usernameError = "Invalid Username") 
            }
            return
        }

        viewModelScope.launch {
            updateSuccessState { it.copy(isSaving = true) }
            delay(1500) // Mock Save API Call
            updateSuccessState { 
                it.copy(isSaving = false, message = "Profile updated successfully!") 
            }
        }
    }

    private fun updateSuccessState(update: (UserProfileUiState.Success) -> UserProfileUiState.Success) {
        val currentState = _uiState.value
        if (currentState is UserProfileUiState.Success) {
            _uiState.value = update(currentState)
        }
    }
}
