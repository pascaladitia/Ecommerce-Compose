package com.pascal.ecommercecompose.ui.screen.home

import com.pascal.ecommercecompose.domain.model.product.ProductDetails

data class HomeUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val product: List<ProductDetails>? = null,
    val category: List<String>? = null
)

data class HomeUIEvent(
    val onSearch: (String) -> Unit = {},
    val onCategory: (String) -> Unit = {},
    val onDetail: (ProductDetails?) -> Unit = {}
)