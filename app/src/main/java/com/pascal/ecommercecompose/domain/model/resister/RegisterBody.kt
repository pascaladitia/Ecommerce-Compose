package com.pascal.ecommercecompose.domain.model.resister

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterBody(
    @SerialName("email")
    val email: String,

    @SerialName("password")
    val password: String,

    @SerialName("nama")
    val nama: String
)