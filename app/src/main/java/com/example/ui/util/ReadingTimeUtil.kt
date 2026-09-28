package com.example.ui.util

import com.example.data.local.ArticleEntity
import kotlin.math.ceil

/**
 * Calculates the estimated reading time in minutes for an article.
 * Uses an average adult reading speed of 200 words per minute.
 */
fun ArticleEntity.estimatedReadingTimeMinutes(): Int {
    val textToAnalyze = when {
        originalContent.isNotBlank() && originalContent.length > summary.length -> originalContent
        summary.isNotBlank() -> "$headline $summary"
        else -> headline
    }

    val words = textToAnalyze.trim().split(Regex("\\s+")).count { it.isNotBlank() }
    val minutes = ceil(words / 200.0).toInt()
    return maxOf(1, minutes)
}

/**
 * Formats reading time into a clean user-facing label (e.g. "1 min read", "3 min read").
 */
fun ArticleEntity.getReadingTimeLabel(): String {
    val minutes = estimatedReadingTimeMinutes()
    return if (minutes == 1) "1 min read" else "$minutes min read"
}
