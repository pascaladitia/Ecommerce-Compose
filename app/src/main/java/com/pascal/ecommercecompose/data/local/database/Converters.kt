package com.pascal.ecommercecompose.data.local.database

import androidx.room.TypeConverter
import com.pascal.ecommercecompose.domain.model.product.Review
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromReviewList(reviews: List<Review>?): String? {
        return reviews?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toReviewList(reviewsString: String?): List<Review>? {
        return reviewsString?.let { json.decodeFromString(it) } ?: emptyList()
    }

    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toStringList(listString: String?): List<String>? {
        return listString?.let { json.decodeFromString(it) } ?: emptyList()
    }
}