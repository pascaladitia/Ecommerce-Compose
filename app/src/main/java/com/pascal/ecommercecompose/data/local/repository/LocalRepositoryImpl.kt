package com.pascal.ecommercecompose.data.local.repository

import com.pascal.ecommercecompose.data.local.entity.ProductEntity
import com.pascal.ecommercecompose.data.local.entity.ProfileEntity


interface LocalRepositoryImpl {
    suspend fun getProfileById(id: Long): ProfileEntity?
    suspend fun getAllProfiles(): List<ProfileEntity>
    suspend fun deleteProfileById(item: ProfileEntity)
    suspend fun insertProfile(item: ProfileEntity)

    suspend fun getProductById(id: Long): ProductEntity?
    suspend fun getAllProducts(): List<ProductEntity>
    suspend fun deleteProductById(item: ProductEntity)
    suspend fun insertProduct(item: ProductEntity)
}