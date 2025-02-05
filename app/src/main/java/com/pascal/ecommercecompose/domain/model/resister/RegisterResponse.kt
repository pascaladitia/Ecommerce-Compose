package com.pascal.ecommercecompose.domain.model.resister

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    @SerialName("message")
    val message: String? = null,

    @SerialName("user_id")
    val userId: Int? = null
)