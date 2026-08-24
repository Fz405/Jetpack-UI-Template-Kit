package com.template.composeuikit.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnboardingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<OnboardingUiState>(OnboardingUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: OnboardingUiEvent) {
        when (event) {
            is OnboardingUiEvent.OnSkipClicked,
            is OnboardingUiEvent.OnFinishClicked -> {
                _uiState.value = OnboardingUiState.NavigateToLogin
            }
            is OnboardingUiEvent.OnNavigationHandled -> {
                _uiState.value = OnboardingUiState.Idle
            }
        }
    }
}