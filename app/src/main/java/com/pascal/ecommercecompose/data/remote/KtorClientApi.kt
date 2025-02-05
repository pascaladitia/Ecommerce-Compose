package com.pascal.ecommercecompose.data.remote

import com.pascal.ecommercecompose.domain.model.dashboard.ResponseDashboard
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Single

@Single
object KtorClientApi {
    suspend fun dashboard(): ResponseDashboard {
        return client.get("http:///dashboard").body()
    }
}