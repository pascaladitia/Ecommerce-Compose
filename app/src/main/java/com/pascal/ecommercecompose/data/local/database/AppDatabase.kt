package com.pascal.ecommercecompose.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pascal.ecommercecompose.data.local.dao.CartDao
import com.pascal.ecommercecompose.data.local.dao.FavoriteDao
import com.pascal.ecommercecompose.data.local.dao.ProductDao
import com.pascal.ecommercecompose.data.local.dao.ProfileDao
import com.pascal.ecommercecompose.data.local.entity.ProductEntity
import com.pascal.ecommercecompose.data.local.entity.ProfileEntity

@Database(
    entities = [ProfileEntity::class, ProductEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun cartDao(): CartDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun productDao(): ProductDao
}

