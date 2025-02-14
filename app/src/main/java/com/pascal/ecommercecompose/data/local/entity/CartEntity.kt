package com.pascal.ecommercecompose.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "cart")
@kotlinx.serialization.Serializable
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String? = null,
    val price: Double? = null,
    val imageID: String? = null,
    val category: String? = null,
    val description: String? = null,
    var qty: Int? = null
) : Serializable
