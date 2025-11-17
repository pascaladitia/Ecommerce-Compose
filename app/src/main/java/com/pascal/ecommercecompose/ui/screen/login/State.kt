package com.pascal.ecommercecompose.ui.screen.login

data class LoginUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val isLogin: Boolean = false
)

data class LoginUIEvent(
    val onLogin: (String, String) -> Unit = { _, _ ->},
    val onGoogle: () -> Unit = {},
    val onRegister: () -> Unit = {}
)