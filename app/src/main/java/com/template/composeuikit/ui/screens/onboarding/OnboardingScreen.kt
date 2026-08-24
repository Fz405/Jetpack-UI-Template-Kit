package com.template.composeuikit.ui.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.template.composeuikit.R
import com.template.composeuikit.ui.components.PrimaryButton
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = viewModel(),
    onNavigateToLogin: () -> Unit // 🛑 No default `{}`!
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        if (state is OnboardingUiState.NavigateToLogin) {
            onNavigateToLogin()
            viewModel.onEvent(OnboardingUiEvent.OnNavigationHandled)
        }
    }

    OnboardingStatelessScreen(
        modifier = modifier,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OnboardingStatelessScreen(
    modifier: Modifier = Modifier,
    onEvent: (OnboardingUiEvent) -> Unit
) {
    val pages = listOf(
        Pair(stringResource(R.string.onboarding_title_1), stringResource(R.string.onboarding_desc_1)),
        Pair(stringResource(R.string.onboarding_title_2), stringResource(R.string.onboarding_desc_2)),
        Pair(stringResource(R.string.onboarding_title_3), stringResource(R.string.onboarding_desc_3))
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp).windowInsetsPadding(WindowInsets.safeDrawing),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onEvent(OnboardingUiEvent.OnSkipClicked) }) {
                    Text(stringResource(R.string.skip))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                Column(
                    modifier = Modifier.fillMaxSize().widthIn(max = 500.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = pages[page].first,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = pages[page].second,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 👈 Swapped new centralized UI component
            PrimaryButton(
                text = if (pagerState.currentPage == pages.size - 1)
                    stringResource(R.string.get_started) else stringResource(R.string.next),
                onClick = {
                    if (pagerState.currentPage < pages.size - 1) {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    } else {
                        onEvent(OnboardingUiEvent.OnFinishClicked)
                    }
                }
            )
        }
    }
}