package com.pascal.ecommercecompose.data.repository

import com.pascal.ecommercecompose.domain.model.dashboard.ResponseDashboard

interface RepositoryImpl {
    suspend fun dashboard() : ResponseDashboard

}