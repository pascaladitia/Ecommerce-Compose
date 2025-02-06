package com.pascal.ecommercecompose.ui.screen.profile

data class ProfileUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = ""
)

data class ProfileUIEvent(
    val onVerified: () -> Unit = {},
    val onLogout: () -> Unit = {},
)