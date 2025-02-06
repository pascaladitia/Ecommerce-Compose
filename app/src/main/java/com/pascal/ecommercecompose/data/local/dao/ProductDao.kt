package com.pascal.ecommercecompose.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pascal.ecommercecompose.data.local.entity.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product WHERE id = :id")
    suspend fun getProductById(id: Long): ProductEntity?

    @Query("SELECT * FROM Product")
    suspend fun getAllProducts(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(entity: ProductEntity)

    @Delete
    suspend fun deleteProduct(entity: ProductEntity)

    @Query("DELETE FROM Product")
    suspend fun clearProduct()
}
