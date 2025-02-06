package com.pascal.ecommercecompose.ui.navigation

sealed class Screen(val route: String) {
    data object SplashScreen: Screen("splash")
    data object LoginScreen: Screen("login")
    data object RegisterScreen: Screen("register")

    data object HomeScreen: Screen("home")
    data object CartScreen: Screen("live")
    data object ProfileScreen: Screen("profile")

    data object DetailScreen: Screen("detail")
    data object ReportScreen: Screen("report")
    data object VerifiedScreen: Screen("verified")
}