package com.pascal.ecommercecompose.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import compose.icons.FeatherIcons
import compose.icons.feathericons.Home
import compose.icons.feathericons.ShoppingCart
import compose.icons.feathericons.User
import compose.icons.feathericons.Video

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color.White)
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val navigationItems = listOf(
                NavigationItem(
                    title = "Home",
                    icon = FeatherIcons.Home,
                    screen = Screen.HomeScreen
                ),
                NavigationItem(
                    title = "Cart",
                    icon = FeatherIcons.ShoppingCart,
                    screen = Screen.CartScreen
                ),
                NavigationItem(
                    title = "Profile",
                    icon = FeatherIcons.User,
                    screen = Screen.ProfileScreen
                )
            )
            navigationItems.map { item ->
                NavigationBarItem(
                    icon = {
                        val iconSize = if (currentRoute == item.screen.route) 28.dp else 24.dp
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.size(iconSize),
                            tint = if (currentRoute == item.screen.route) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    },
                    label = {
                        if (currentRoute == item.screen.route) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontSize = 10.sp
                                ),
                                color = Color.White
                            )
                        }

                    },
                    alwaysShowLabel = false,
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
