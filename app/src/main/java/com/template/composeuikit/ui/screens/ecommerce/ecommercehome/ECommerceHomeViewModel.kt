package com.template.composeuikit.ui.screens.ecommerce.ecommercehome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ECommerceHomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ECommerceHomeUiState>(ECommerceHomeUiState.Loading)
    val uiState: StateFlow<ECommerceHomeUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = ECommerceHomeUiState.Loading
            delay(1000) // Simulating network request
            _uiState.value = ECommerceHomeUiState.Success()
        }
    }

    fun onEvent(event: ECommerceHomeUiEvent) {
        when (event) {
            is ECommerceHomeUiEvent.OnProductClicked -> {
                // In the future, this might trigger a navigation event state
            }
            is ECommerceHomeUiEvent.OnRefreshClicked -> {
                loadInitialData()
            }
        }
    }
}