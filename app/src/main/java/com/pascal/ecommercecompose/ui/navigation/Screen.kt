package com.pascal.ecommercecompose.ui.navigation

sealed class Screen(val route: String) {
    data object SplashScreen: Screen("splash")
    data object HomeScreen: Screen("home")
    data object LiveScreen: Screen("live")
    data object ProfileScreen: Screen("profile")
}