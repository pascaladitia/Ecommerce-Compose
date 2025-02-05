package com.pascal.ecommercecompose.data.repository

import com.pascal.ecommercecompose.data.remote.KtorClientApi
import com.pascal.ecommercecompose.domain.model.dashboard.ResponseDashboard
import org.koin.core.annotation.Single

@Single
class Repository : RepositoryImpl {
    override suspend fun dashboard(): ResponseDashboard {
        return KtorClientApi.dashboard()
    }
}