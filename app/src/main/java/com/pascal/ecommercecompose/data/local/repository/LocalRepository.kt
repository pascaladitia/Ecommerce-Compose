package com.pascal.ecommercecompose.data.local.repository

import com.pascal.ecommercecompose.data.local.database.AppDatabase
import com.pascal.ecommercecompose.data.local.entity.ProductEntity
import com.pascal.ecommercecompose.data.local.entity.ProfileEntity
import org.koin.core.annotation.Single

@Single
class LocalRepository(
    private val database: AppDatabase,
) : LocalRepositoryImpl {

    // Profile
    override suspend fun getProfileById(id: Long): ProfileEntity? {
        return database.profileDao().getProfileById(id)
    }

    override suspend fun getAllProfiles(): List<ProfileEntity> {
        return database.profileDao().getAllProfiles()
    }

    override suspend fun deleteProfileById(item: ProfileEntity) {
        return database.profileDao().deleteProfile(item)
    }

    override suspend fun insertProfile(item: ProfileEntity) {
        return database.profileDao().insertProfile(item)
    }

    // Cart
    override suspend fun getCartById(id: Long): ProductEntity? {
        return database.cartDao().getCartById(id)
    }

    override suspend fun getAllCart(): List<ProductEntity> {
        return database.cartDao().getAllCart()
    }

    override suspend fun deleteCartById(item: ProductEntity) {
        return database.cartDao().deleteCart(item)
    }

    override suspend fun deleteCart() {
        return database.cartDao().clearCart()
    }

    override suspend fun insertCart(item: ProductEntity) {
        return database.cartDao().insertCart(item)
    }

    // Favorite
    override suspend fun getFavoriteById(id: Long): ProductEntity? {
        return database.favoriteDao().getFavoriteById(id)
    }

    override suspend fun getAllFavorite(): List<ProductEntity> {
        return database.favoriteDao().getAllFavorites()
    }

    override suspend fun deleteFavoriteById(item: ProductEntity) {
        return database.favoriteDao().deleteFavorite(item)
    }

    override suspend fun deleteFavorite() {
        return database.favoriteDao().clearFavorite()
    }

    override suspend fun insertFavorite(item: ProductEntity) {
        return database.favoriteDao().insertFavorite(item)
    }

    // Product
    override suspend fun getProductById(id: Long): ProductEntity? {
        return database.productDao().getProductById(id)
    }

    override suspend fun getAllProduct(): List<ProductEntity> {
        return database.productDao().getAllProducts()
    }

    override suspend fun deleteProductById(item: ProductEntity) {
        return database.productDao().deleteProduct(item)
    }

    override suspend fun deleteProduct() {
        return database.productDao().clearProduct()
    }

    override suspend fun insertProduct(item: ProductEntity) {
        return database.productDao().insertProduct(item)
    }
    
}