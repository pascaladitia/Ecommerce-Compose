package com.pascal.ecommercecompose.ui.screen.detail

import com.pascal.ecommercecompose.data.local.entity.ProductEntity

data class DetailUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val isSuccess: Boolean = false,
    val product: ProductEntity? = null
)

data class DetailUIEvent(
    val onCart: (ProductEntity?) -> Unit = {},
    val onFavorite: (Boolean, ProductEntity?) -> Unit = { _, _ ->},
    val onNavBack: () -> Unit = {},
)