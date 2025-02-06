package com.pascal.ecommercecompose.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "product")
@kotlinx.serialization.Serializable
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val price: Double,
    val isliked: Int,
    val imageID: Int,
    val category: String,
    val description: String,
    var qty: Int
) : Serializable
