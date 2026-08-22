package com.template.composeuikit.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.template.composeuikit.ui.screens.auth.login.LoginScreen
import com.template.composeuikit.ui.screens.auth.signup.SignUpScreen

// We use an extension function to keep the graph modular and scalable
fun NavGraphBuilder.authGraph(
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    // Look here! We are using your Screen sealed class instead of raw strings
    composable(route = Screen.Login.route) {
        LoginScreen(
            onNavigateToSignUp = onNavigateToSignUp,
            onNavigateToHome = onNavigateToHome
        )
    }

    composable(route = Screen.SignUp.route) {
        SignUpScreen(
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToOtp = { email ->
                // We will route this to Otp when we build it next!
            }
        )
    }
}