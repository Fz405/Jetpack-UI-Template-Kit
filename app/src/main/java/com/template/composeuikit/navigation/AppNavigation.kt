package com.template.composeuikit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    // 1. Initialize the root navigation controller
    val navController = rememberNavController()

    // 2. Set the starting destination using your Screen dictionary
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
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
            }
        )
    }
}