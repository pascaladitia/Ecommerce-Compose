package com.pascal.ecommercecompose.domain.model.user

import java.io.Serializable

@kotlinx.serialization.Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val photo_url: String = "",
    val isVerified: Boolean = false
) : Serializable
