package com.template.composeuikit.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.template.composeuikit.ui.screens.auth.forgotpassword.ForgotPasswordScreen
import com.template.composeuikit.ui.screens.auth.login.LoginScreen
import com.template.composeuikit.ui.screens.auth.otp.OtpScreen
import com.template.composeuikit.ui.screens.auth.signup.SignUpScreen

// We use an extension function to keep the graph modular and scalable
fun NavGraphBuilder.authGraph(
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToOtp: (String) -> Unit,

) {

    composable(
        route = Screen.Otp.route,
        arguments = listOf(navArgument("email") { type = NavType.StringType })
    ) { backStackEntry ->
        val email = backStackEntry.arguments?.getString("email") ?: ""

        OtpScreen(
            email = email,
            onNavigateBack = onNavigateBack,
            onNavigateToHome = onNavigateToHome
        )
    }
    // Look here! We are using your Screen sealed class instead of raw strings
    composable(route = Screen.Login.route) {
        LoginScreen(
            onNavigateToSignUp = onNavigateToSignUp,
            onNavigateToHome = onNavigateToHome,
            onNavigateToForgotPassword = onNavigateToForgotPassword,
            onNavigateToOtp = onNavigateToOtp
        )
    }

    composable(route = Screen.SignUp.route) {
        SignUpScreen(
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToOtp = onNavigateToOtp
        )
    }

      composable(route = Screen.ForgotPassword.route) {
        ForgotPasswordScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToOtp = onNavigateToOtp
        )
    }
}

fun NavGraphBuilder.socialGraph(
    onNavigateBack: () -> Unit
) {
    composable(route = Screen.UserProfile.route) {
        com.template.composeuikit.ui.screens.social.user_profile.UserProfileScreen(
            onNavigateUp = onNavigateBack
        )
    }
}