package com.example.ui.util

import com.example.data.local.ArticleEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateTimeUtil {
    const val TWENTY_FOUR_HOURS_MS: Long = 24 * 60 * 60 * 1000L

    private val datePatterns = listOf(
        "EEE, dd MMM yyyy HH:mm:ss Z",       // RFC-822 with numeric zone: Mon, 28 Sep 2026 08:35:04 +0530
        "EEE, dd MMM yyyy HH:mm:ss z",       // RFC-822 with named zone: Mon, 28 Sep 2026 08:35:04 GMT
        "EEE, dd MMM yyyy HH:mm:ss zzz",
        "EEE, dd MMM yyyy HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",      // ISO-8601 UTC with ms
        "yyyy-MM-dd'T'HH:mm:ss'Z'",          // ISO-8601 UTC
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",      // ISO-8601 with offset
        "yyyy-MM-dd'T'HH:mm:ssXXX",          // ISO-8601 with offset
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd HH:mm:ss",
        "dd MMM yyyy HH:mm:ss"
    )

    private val displayFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    private val fullDisplayFormat = SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale.getDefault())
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    fun parseDate(dateStr: String): Date? {
        val trimmed = dateStr.trim()
        if (trimmed.isBlank()) return null

        for (pattern in datePatterns) {
            try {
                val format = SimpleDateFormat(pattern, Locale.ENGLISH).apply {
                    timeZone = TimeZone.getTimeZone("GMT")
                }
                val parsed = format.parse(trimmed)
                if (parsed != null) return parsed
            } catch (e: Exception) {
                // Try next pattern
            }
        }
        return null
    }

    fun formatIso(timeMs: Long): String {
        return isoFormat.format(Date(timeMs))
    }

    fun getArticleTimestamp(article: ArticleEntity): Long {
        val parsed = parseDate(article.publishedAt)
        return parsed?.time ?: article.createdAt
    }

    fun isArticleWithin24Hours(article: ArticleEntity, maxAgeMs: Long = TWENTY_FOUR_HOURS_MS): Boolean {
        val timestamp = getArticleTimestamp(article)
        val now = System.currentTimeMillis()
        // Allow up to 1 hour ahead (clock skew tolerance) and strictly up to maxAgeMs (24h) in past
        val age = now - timestamp
        return age in -3_600_000L..maxAgeMs
    }

    fun formatArticleDateTime(article: ArticleEntity): String {
        val timestamp = getArticleTimestamp(article)
        val now = System.currentTimeMillis()
        val diffMs = now - timestamp

        if (diffMs in 0..180_000) {
            return "Just now"
        }
        val diffMinutes = diffMs / 60_000
        if (diffMinutes in 3..59) {
            return "${diffMinutes}m ago"
        }
        val diffHours = diffMinutes / 60
        if (diffHours in 1..23) {
            return "${diffHours}h ago"
        }

        return displayFormat.format(Date(timestamp))
    }

    fun formatFullDateTime(article: ArticleEntity): String {
        val timestamp = getArticleTimestamp(article)
        return fullDisplayFormat.format(Date(timestamp))
    }
}

fun ArticleEntity.getFormattedDateTime(): String = DateTimeUtil.formatArticleDateTime(this)
fun ArticleEntity.getFullFormattedDateTime(): String = DateTimeUtil.formatFullDateTime(this)
fun ArticleEntity.isWithin24Hours(): Boolean = DateTimeUtil.isArticleWithin24Hours(this)
