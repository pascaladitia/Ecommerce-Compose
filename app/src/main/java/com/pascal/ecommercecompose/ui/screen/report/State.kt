package com.pascal.ecommercecompose.ui.screen.report

import com.pascal.ecommercecompose.data.local.entity.CartEntity

data class ReportUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val isReport: Boolean = false
)

data class ReportUIEvent(
    val onDownload: (List<CartEntity>?) -> Unit = {},
    val onNavBack: () -> Unit = {},
)