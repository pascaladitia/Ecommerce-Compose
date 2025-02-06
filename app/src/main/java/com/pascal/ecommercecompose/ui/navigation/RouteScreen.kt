package com.pascal.ecommercecompose.ui.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pascal.ecommercecompose.data.prefs.PreferencesLogin
import com.pascal.ecommercecompose.ui.screen.detail.DetailScreen
import com.pascal.ecommercecompose.ui.screen.home.HomeScreen
import com.pascal.ecommercecompose.ui.screen.cart.CartScreen
import com.pascal.ecommercecompose.ui.screen.login.LoginScreen
import com.pascal.ecommercecompose.ui.screen.profile.ProfileScreen
import com.pascal.ecommercecompose.ui.screen.register.RegisterScreen
import com.pascal.ecommercecompose.ui.screen.splash.SplashScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun RouteScreen(
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(
                    Screen.HomeScreen.route,
                    Screen.CartScreen.route,
                    Screen.ProfileScreen.route
                )) {
                BottomBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.SplashScreen.route
        ) {
            composable(route = Screen.SplashScreen.route) {
                SplashScreen(
                    paddingValues = paddingValues
                ) {
                    val isLogin = PreferencesLogin.getIsLogin(context)

                    navController.navigate(
                        if (isLogin) Screen.HomeScreen.route else Screen.LoginScreen.route
                    ) {
                        popUpTo(Screen.SplashScreen.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
            composable(route = Screen.LoginScreen.route) {
                LoginScreen(
                    paddingValues = paddingValues,
                    onLogin = {
                        navController.navigate(Screen.HomeScreen.route) {
                            popUpTo(Screen.LoginScreen.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                    onRegister = {
                        navController.navigate(Screen.RegisterScreen.route)
                    }
                )
            }
            composable(route = Screen.RegisterScreen.route) {
                RegisterScreen(
                    paddingValues = paddingValues,
                    onNavBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(route = Screen.HomeScreen.route) {
                HomeScreen(
                    paddingValues = paddingValues,
                    onDetail = {
                        saveToCurrentBackStack(navController, "item", it)
                        navController.navigate(Screen.DetailScreen.route)
                    }
                )
            }
            composable(route = Screen.CartScreen.route) {
                CartScreen(
                    paddingValues = paddingValues,
                    onDetail = {
                        navController.popBackStack()
                    }
                )
            }
            composable(route = Screen.ProfileScreen.route) {
                ProfileScreen(
                    paddingValues = paddingValues,
                    onDetail = {
                        navController.popBackStack()
                    }
                )
            }
            composable(route = Screen.DetailScreen.route) {
                DetailScreen(
                    paddingValues = paddingValues,
                    product = getFromPreviousBackStack(navController, "item"),
                    onNavBack = {
                        navController.popBackStack()
                        navController.navigate(Screen.HomeScreen.route)
                    }
                )
            }
        }
    }
}

inline fun <reified T> saveToCurrentBackStack(
    navController: NavController,
    key: String,
    data: T
) {
    val json = Json.encodeToString(data)
    navController.currentBackStackEntry?.savedStateHandle?.set(key, json)
}

inline fun <reified T> getFromPreviousBackStack(
    navController: NavController,
    key: String
): T? {
    val json = navController.previousBackStackEntry?.savedStateHandle?.get<String>(key)
    return json?.let { Json.decodeFromString(it) }
}


