package com.pascal.ecommercecompose.ui.screen.home

import com.pascal.ecommercecompose.domain.model.product.ProductDetails
import com.pascal.ecommercecompose.domain.model.product.ProductResponse

data class HomeUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val data: ProductResponse? = null
)

data class HomeUIEvent(
    val onDetail: (ProductDetails?) -> Unit = {}
)