package com.pascal.ecommercecompose.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pascal.ecommercecompose.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM Favorite WHERE id = :id")
    suspend fun getFavoriteById(id: Long): FavoriteEntity?

    @Query("SELECT * FROM Favorite")
    suspend fun getAllFavorites(): List<FavoriteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(entity: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(entity: FavoriteEntity)

    @Query("DELETE FROM Favorite")
    suspend fun clearFavorite()
}
