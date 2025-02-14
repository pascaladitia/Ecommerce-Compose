package com.pascal.ecommercecompose.ui.screen.detail

import com.pascal.ecommercecompose.domain.model.product.ProductDetails

data class DetailUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val isSuccess: Boolean = false,
    val product: ProductDetails? = null
)

data class DetailUIEvent(
    val onCart: (ProductDetails?) -> Unit = {},
    val onNavBack: () -> Unit = {},
)