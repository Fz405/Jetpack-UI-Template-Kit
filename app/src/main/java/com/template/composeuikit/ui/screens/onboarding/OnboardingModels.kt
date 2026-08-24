package com.template.composeuikit.ui.screens.onboarding

sealed interface OnboardingUiState {
    data object Idle : OnboardingUiState
    data object NavigateToLogin : OnboardingUiState
}

sealed interface OnboardingUiEvent {
    data object OnSkipClicked : OnboardingUiEvent
    data object OnFinishClicked : OnboardingUiEvent
    data object OnNavigationHandled : OnboardingUiEvent
}