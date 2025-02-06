package com.pascal.ecommercecompose.domain.model.dummy

import java.io.Serializable

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val isliked: Int,
    val imageID: Int,
    val category: String,
) : Serializable
