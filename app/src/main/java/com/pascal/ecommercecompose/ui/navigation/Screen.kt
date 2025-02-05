package com.pascal.ecommercecompose.ui.navigation

sealed class Screen(val route: String) {
    data object SplashScreen: Screen("splash")
    data object LoginScreen: Screen("login")
    data object RegisterScreen: Screen("register")

    data object HomeScreen: Screen("home")
    data object LiveScreen: Screen("live")
    data object ProfileScreen: Screen("profile")
}