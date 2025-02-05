package com.pascal.ecommercecompose.domain.model.dashboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDashboard(

	@SerialName("code")
	val code: Int? = null,

	@SerialName("success")
	val success: Boolean? = null,

	@SerialName("message")
	val message: String? = null
)