package com.template.composeuikit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.template.composeuikit.ui.screens.main.MainScreenLayout
import com.template.composeuikit.ui.screens.onboarding.OnboardingScreen

@Composable
fun AppNavigation() {
    // 1. Initialize the root navigation controller
    val navController = rememberNavController()

    // 2. Set the starting destination using your Screen dictionary
    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route
    ) {
        // 👈 1. ADD THIS BLOCK: The Onboarding Entry Point
        composable(route = Screen.Onboarding.route) {
            OnboardingScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        // Pops Onboarding off the stack so the user can't press back to it
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        // 3. Attach the modular Auth Graph
        authGraph(
            onNavigateToSignUp = {
                navController.navigate(Screen.SignUp.route)
            },
            onNavigateToHome = {
                navController.navigate(Screen.MainScreen.route) {
                    // This clears the backstack so the user can't press "Back" to go to the login screen
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            },
            onNavigateToLogin = {
                // Safely pops the SignUp screen off, returning the user to the existing Login screen
                navController.popBackStack()
            },
            onNavigateToForgotPassword = {
                // Pushes the Forgot Password screen on top of Login
                navController.navigate(Screen.ForgotPassword.route)
            },
            onNavigateBack = {
                // Handles the back arrow on the Forgot Password screen
                navController.popBackStack()
            },
            onNavigateToOtp = { email ->
                // Uses the helper function you built in Screen.kt to pass the email safely!
                navController.navigate(Screen.Otp.createRoute(email))
            },

        )
        composable(route = Screen.MainScreen.route) {
            MainScreenLayout()
        }
    }
}