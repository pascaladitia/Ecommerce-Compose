package com.pascal.ecommercecompose.ui.screen.verified

data class VerifiedUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val isVerified: Boolean = false
)

data class VerifiedUIEvent(
    val onNavBack: () -> Unit = {},
)