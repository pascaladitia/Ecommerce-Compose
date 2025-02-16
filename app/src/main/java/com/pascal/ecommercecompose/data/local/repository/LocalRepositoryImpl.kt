package com.pascal.ecommercecompose.data.local.repository

import com.pascal.ecommercecompose.data.local.entity.CartEntity
import com.pascal.ecommercecompose.data.local.entity.FavoriteEntity
import com.pascal.ecommercecompose.data.local.entity.ProductEntity
import com.pascal.ecommercecompose.data.local.entity.ProfileEntity


interface LocalRepositoryImpl {
    suspend fun getProfileById(id: Long): ProfileEntity?
    suspend fun getAllProfiles(): List<ProfileEntity>
    suspend fun deleteProfileById(item: ProfileEntity)
    suspend fun insertProfile(item: ProfileEntity)

    suspend fun getCartById(id: Long): CartEntity?
    suspend fun getAllCart(): List<CartEntity>
    suspend fun deleteCartById(item: CartEntity)
    suspend fun deleteCart()
    suspend fun insertCart(item: CartEntity)

    suspend fun getFavoriteById(id: Long): FavoriteEntity?
    suspend fun getAllFavorite(): List<FavoriteEntity>
    suspend fun deleteFavoriteById(item: FavoriteEntity)
    suspend fun deleteFavorite()
    suspend fun insertFavorite(item: FavoriteEntity)

    suspend fun getProductById(id: Int): ProductEntity?
    suspend fun getAllProduct(): List<ProductEntity>
    suspend fun deleteProductById(item: ProductEntity)
    suspend fun deleteProduct()
    suspend fun insertProduct(item: ProductEntity)
}