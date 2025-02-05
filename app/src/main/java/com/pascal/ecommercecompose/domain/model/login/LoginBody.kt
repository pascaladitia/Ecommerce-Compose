package com.pascal.ecommercecompose.domain.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginBody(
    @SerialName("email")
    val email: String,

    @SerialName("password")
    val password: String
)