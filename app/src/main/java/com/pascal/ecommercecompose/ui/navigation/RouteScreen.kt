package com.pascal.ecommercecompose.ui.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pascal.ecommercecompose.ui.screen.home.HomeScreen
import com.pascal.ecommercecompose.ui.screen.live.LiveScreen
import com.pascal.ecommercecompose.ui.screen.profile.ProfileScreen
import com.pascal.ecommercecompose.ui.screen.splash.SplashScreen

@Composable
fun RouteScreen(
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(
                    Screen.HomeScreen.route,
                    Screen.LiveScreen.route,
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
                    navController.navigate( Screen.HomeScreen.route) {
                        popUpTo(Screen.SplashScreen.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
            composable(route = Screen.HomeScreen.route) {
                HomeScreen(
                    paddingValues = paddingValues,
                    onDetail = {
                        navController.popBackStack()
                    }
                )
            }
            composable(route = Screen.LiveScreen.route) {
                LiveScreen(
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
        }
    }
}
