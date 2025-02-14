package com.pascal.ecommercecompose.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pascal.ecommercecompose.data.local.entity.ProductEntity

@Dao
interface CartDao {
    @Query("SELECT * FROM Product WHERE id = :id")
    suspend fun getCartById(id: Long): ProductEntity?

    @Query("SELECT * FROM Product")
    suspend fun getAllCart(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(entity: ProductEntity)

    @Delete
    suspend fun deleteCart(entity: ProductEntity)

    @Query("DELETE FROM Product")
    suspend fun clearCart()
}
