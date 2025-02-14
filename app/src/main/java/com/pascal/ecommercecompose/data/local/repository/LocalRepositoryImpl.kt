package com.pascal.ecommercecompose.data.local.repository

import com.pascal.ecommercecompose.data.local.entity.ProductEntity
import com.pascal.ecommercecompose.data.local.entity.ProfileEntity


interface LocalRepositoryImpl {
    suspend fun getProfileById(id: Long): ProfileEntity?
    suspend fun getAllProfiles(): List<ProfileEntity>
    suspend fun deleteProfileById(item: ProfileEntity)
    suspend fun insertProfile(item: ProfileEntity)

    suspend fun getCartById(id: Long): ProductEntity?
    suspend fun getAllCart(): List<ProductEntity>
    suspend fun deleteCartById(item: ProductEntity)
    suspend fun deleteCart()
    suspend fun insertCart(item: ProductEntity)

    suspend fun getFavoriteById(id: Long): ProductEntity?
    suspend fun getAllFavorite(): List<ProductEntity>
    suspend fun deleteFavoriteById(item: ProductEntity)
    suspend fun deleteFavorite()
    suspend fun insertFavorite(item: ProductEntity)

    suspend fun getProductById(id: Long): ProductEntity?
    suspend fun getAllProduct(): List<ProductEntity>
    suspend fun deleteProductById(item: ProductEntity)
    suspend fun deleteProduct()
    suspend fun insertProduct(item: ProductEntity)
}