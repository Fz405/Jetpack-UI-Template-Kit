package com.template.composeuikit.ui.screens.social.user_profile

sealed interface UserProfileUiState {
    data object Loading : UserProfileUiState
    data class Success(
        val username: String = "",
        val bio: String = "",
        val usernameError: String? = null,
        val isSaving: Boolean = false,
        val message: String? = null
    ) : UserProfileUiState
    data class Error(val message: String) : UserProfileUiState
}

sealed interface UserProfileUiEvent {
    data class OnUsernameChanged(val newValue: String) : UserProfileUiEvent
    data class OnBioChanged(val newValue: String) : UserProfileUiEvent
    data object OnSaveClicked : UserProfileUiEvent
    data object OnDismissMessage : UserProfileUiEvent
}
