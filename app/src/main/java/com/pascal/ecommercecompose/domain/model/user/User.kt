package com.pascal.ecommercecompose.domain.model.user

import java.io.Serializable

@kotlinx.serialization.Serializable
data class User(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val photo_url: String? = null
) : Serializable
