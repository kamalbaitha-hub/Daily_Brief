package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "articles",
    indices = [
        Index(value = ["category"]),
        Index(value = ["sourceUrl"], unique = true)
    ]
)
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String,
    val headline: String,
    val summary: String,
    val originalContent: String = "",
    val imageUrl: String = "",
    val source: String = "",
    val sourceUrl: String = "",
    val publishedAt: String = "",
    val isCached: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
