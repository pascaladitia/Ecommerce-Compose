package com.pascal.ecommercecompose.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pascal.ecommercecompose.data.local.entity.ProductEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM Product WHERE id = :id")
    suspend fun getFavoriteById(id: Long): ProductEntity?

    @Query("SELECT * FROM Product")
    suspend fun getAllFavorites(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(entity: ProductEntity)

    @Delete
    suspend fun deleteFavorite(entity: ProductEntity)

    @Query("DELETE FROM Product")
    suspend fun clearFavorite()
}
