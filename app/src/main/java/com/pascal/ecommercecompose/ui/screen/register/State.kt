package com.pascal.ecommercecompose.ui.screen.register

data class RegisterUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val isRegister: Boolean = false
)

data class RegisterUIEvent(
    val onRegister: (String, String, String) -> Unit = { _, _, _ ->},
    val onNavBack: () -> Unit = {},
)