package com.template.composeuikit.navigation

// Enterprise Standard: Using a sealed class prevents typos when typing route names
sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding_screen")
    object Login : Screen("login_screen")
    object SignUp : Screen("signup_screen")
    object ForgotPassword : Screen("forgot_password_screen")
    object Otp : Screen("otp_screen/{email}") {
        // Helper to pass the email argument to the OTP screen
        fun createRoute(email: String) = "otp_screen/$email"
    }
    object MainScreen : Screen("Main_screen")

    // E-Commerce
    object ECommerceHome : Screen("ecommerce_home")

    // Finance
    object WalletDashboard : Screen("wallet_dashboard")
    object CardManagement : Screen("card_management")
    object TransactionHistory : Screen("transaction_history")
    object TransferMoney : Screen("transfer_money")

    // Social
    object UserProfile : Screen("user_profile")
    object Settings : Screen("settings")
    object NotificationCenter : Screen("notification_center")
    object Messaging : Screen("messaging")
}