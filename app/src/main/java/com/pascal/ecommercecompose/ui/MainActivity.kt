package com.pascal.ecommercecompose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pascal.ecommercecompose.ui.navigation.RouteScreen
import com.pascal.ecommercecompose.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                RouteScreen()
            }
        }
    }
}