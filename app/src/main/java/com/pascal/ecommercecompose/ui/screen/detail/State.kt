package com.pascal.ecommercecompose.ui.screen.detail

import com.pascal.ecommercecompose.domain.model.dummy.Product

data class DetailUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val isSuccess: Boolean = false
)

data class DetailUIEvent(
    val onCart: (Product?) -> Unit = {},
    val onNavBack: () -> Unit = {},
)