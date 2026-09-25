package com.example.ui.util

import com.example.data.local.ArticleEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateTimeUtil {
    private val rfc822Format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH).apply {
        timeZone = TimeZone.getTimeZone("GMT")
    }
    private val iso8601Format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private val displayFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    private val fullDisplayFormat = SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale.getDefault())

    fun parseDate(dateStr: String): Date? {
        if (dateStr.isBlank()) return null
        return try {
            rfc822Format.parse(dateStr)
        } catch (e: Exception) {
            try {
                iso8601Format.parse(dateStr)
            } catch (e2: Exception) {
                null
            }
        }
    }

    fun formatArticleDateTime(article: ArticleEntity): String {
        val parsed = parseDate(article.publishedAt)
        val timestamp = parsed?.time ?: article.createdAt
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
        val parsed = parseDate(article.publishedAt)
        val timestamp = parsed?.time ?: article.createdAt
        return fullDisplayFormat.format(Date(timestamp))
    }
}

fun ArticleEntity.getFormattedDateTime(): String = DateTimeUtil.formatArticleDateTime(this)
fun ArticleEntity.getFullFormattedDateTime(): String = DateTimeUtil.formatFullDateTime(this)
