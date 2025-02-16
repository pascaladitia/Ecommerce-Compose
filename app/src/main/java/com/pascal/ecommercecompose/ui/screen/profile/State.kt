package com.pascal.ecommercecompose.ui.screen.profile

data class ProfileUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val isVerified: Boolean = false
)

data class ProfileUIEvent(
    val onVerified: () -> Unit = {},
    val onLogout: () -> Unit = {},
)