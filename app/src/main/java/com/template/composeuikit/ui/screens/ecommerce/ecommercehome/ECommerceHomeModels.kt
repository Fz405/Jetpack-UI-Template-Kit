package com.template.composeuikit.ui.screens.ecommerce.ecommercehome

import com.template.composeuikit.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class Product(
    val id: String,
    val nameResId: Int
)

sealed interface ECommerceHomeUiState {
    data object Loading : ECommerceHomeUiState

    data class Success(
        val products: ImmutableList<Product> = persistentListOf(
            Product(id = "1", nameResId = R.string.product_mock_1),
            Product(id = "2", nameResId = R.string.product_mock_2),
            Product(id = "3", nameResId = R.string.product_mock_3)
        )
    ) : ECommerceHomeUiState

    data class Error(val message: String) : ECommerceHomeUiState
}

sealed interface ECommerceHomeUiEvent {
    data class OnProductClicked(val productId: String) : ECommerceHomeUiEvent
    data object OnRefreshClicked : ECommerceHomeUiEvent
}