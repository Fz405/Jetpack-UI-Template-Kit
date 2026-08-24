package com.template.composeuikit.ui.screens.ecommerce.ecommercehome

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.template.composeuikit.R

// 1. Stateful Wrapper
@Composable
fun ECommerceHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: ECommerceHomeViewModel = viewModel(),
    onNavigateToProductDetails: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ECommerceHomeStatelessScreen(
        modifier = modifier,
        state = state,
        onEvent = { event ->
            when (event) {
                is ECommerceHomeUiEvent.OnProductClicked -> onNavigateToProductDetails(event.productId)
                else -> viewModel.onEvent(event)
            }
        }
    )
}

// 2. Stateless UI
@Composable
private fun ECommerceHomeStatelessScreen(
    modifier: Modifier = Modifier,
    state: ECommerceHomeUiState,
    onEvent: (ECommerceHomeUiEvent) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding() // Ensures we respect system insets
    ) {
        when (state) {
            is ECommerceHomeUiState.Loading -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = stringResource(R.string.ecommerce_home_loading))
                }
            }
            is ECommerceHomeUiState.Error -> {
                Text(
                    text = state.message.ifBlank { stringResource(R.string.ecommerce_home_error) },
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is ECommerceHomeUiState.Success -> {
                if (state.products.isEmpty()) {
                    Text(
                        text = stringResource(R.string.ecommerce_home_empty),
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.products,
                            key = { it.id } // 👈 performance optimization: keying by ID
                        ) { product ->
                            ProductItem(
                                // 👇 Resolves the resource ID safely in the UI layer
                                text = stringResource(id = product.nameResId),
                                onClick = { onEvent(ECommerceHomeUiEvent.OnProductClicked(product.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// 3. Sub-components
@Composable
private fun ProductItem(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Card(
        // 👈 FIX: Replaced experimental Card(onClick) with a stable Modifier.clickable
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text)
        }
    }
}

// 4. Previews
@Preview(showBackground = true)
@Composable
private fun ECommerceHomeScreenPreview_Success() {
    MaterialTheme {
        ECommerceHomeStatelessScreen(
            state = ECommerceHomeUiState.Success(),
            onEvent = {}
        )
    }
}