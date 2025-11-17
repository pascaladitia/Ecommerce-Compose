package com.pascal.ecommercecompose.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pascal.ecommercecompose.data.local.entity.CartEntity

@Dao
interface CartDao {
    @Query("SELECT * FROM cart WHERE id = :id")
    suspend fun getCartById(id: Long): CartEntity?

    @Query("SELECT * FROM cart")
    suspend fun getAllCart(): List<CartEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(entity: CartEntity)

    @Delete
    suspend fun deleteCart(entity: CartEntity)

    @Query("DELETE FROM cart")
    suspend fun clearCart()
}
