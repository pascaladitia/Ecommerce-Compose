package com.pascal.ecommercecompose.domain.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("message")
    val message: String? = null,

    @SerialName("user_id")
    val userId: Int? = null,

    @SerialName("nama")
    val name: String? = null
)