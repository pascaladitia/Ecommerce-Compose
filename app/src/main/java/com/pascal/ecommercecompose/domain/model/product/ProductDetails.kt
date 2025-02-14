package com.pascal.ecommercecompose.domain.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ProductDetails(
    val availabilityStatus: String? = null,
    val brand: String? = null,
    val category: String? = null,
    val description: String? = null,
    val discountPercentage: Double? = null,
    val id: Int? = null,
    val images: List<String>? = null,
    val minimumOrderQuantity: Int? = null,
    val price: Double? = null,
    val rating: Double? = null,
    val returnPolicy: String? = null,
    val reviews: List<Review>? = null,
    val shippingInformation: String? = null,
    val sku: String? = null,
    val stock: Int? = null,
    val tags: List<String>? = null,
    val thumbnail: String? = null,
    val title: String? = null,
    val warrantyInformation: String? = null,
    val weight: Int? = null,
    var isFavorite: Boolean? = null
)