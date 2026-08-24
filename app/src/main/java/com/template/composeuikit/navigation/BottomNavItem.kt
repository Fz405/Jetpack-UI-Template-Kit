package com.template.composeuikit.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.template.composeuikit.R

sealed class BottomNavItem(
    val route: String,
    @StringRes val titleResId: Int,
    val icon: ImageVector
) {
    object ECommerce : BottomNavItem(
        route = Screen.ECommerceHome.route,
        titleResId = R.string.nav_shop,
        icon = Icons.Default.ShoppingCart
    )
    object Finance : BottomNavItem(
        route = Screen.WalletDashboard.route,
        titleResId = R.string.nav_wallet,
        icon = Icons.Default.AccountBalanceWallet
    )
    object Social : BottomNavItem(
        route = Screen.UserProfile.route,
        titleResId = R.string.nav_profile,
        icon = Icons.Default.Person
    )
}