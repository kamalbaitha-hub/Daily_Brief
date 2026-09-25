package com.example.data.remote

import android.util.Log
import com.example.data.local.ArticleEntity
import com.example.ui.util.DateTimeUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

object RealtimeNewsFetcher {
    private const val TAG = "RealtimeNewsFetcher"

    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    data class CategoryFeed(
        val categoryName: String,
        val rssUrl: String,
        val fallbackImage: String
    )

    private val FEEDS = listOf(
        CategoryFeed(
            categoryName = "Global markets & economy",
            rssUrl = "https://news.google.com/rss/search?q=global+markets+economy&hl=en-US&gl=US&ceid=US:en",
            fallbackImage = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=800&auto=format&fit=crop&q=80"
        ),
        CategoryFeed(
            categoryName = "Indian stock market & IPO",
            rssUrl = "https://news.google.com/rss/search?q=Indian+stock+market+Nifty+IPO&hl=en-IN&gl=IN&ceid=IN:en",
            fallbackImage = "https://images.unsplash.com/photo-1590283603385-17ffb3a7f29f?w=800&auto=format&fit=crop&q=80"
        ),
        CategoryFeed(
            categoryName = "Railways & infrastructure",
            rssUrl = "https://news.google.com/rss/search?q=Indian+Railways+infrastructure+metro&hl=en-IN&gl=IN&ceid=IN:en",
            fallbackImage = "https://images.unsplash.com/photo-1474487548417-781cb71495f3?w=800&auto=format&fit=crop&q=80"
        ),
        CategoryFeed(
            categoryName = "Technology & AI",
            rssUrl = "https://news.google.com/rss/search?q=artificial+intelligence+technology+AI&hl=en-US&gl=US&ceid=US:en",
            fallbackImage = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&auto=format&fit=crop&q=80"
        ),
        CategoryFeed(
            categoryName = "Health & fitness",
            rssUrl = "https://news.google.com/rss/search?q=health+fitness+wellness+medicine&hl=en-US&gl=US&ceid=US:en",
            fallbackImage = "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800&auto=format&fit=crop&q=80"
        ),
        CategoryFeed(
            categoryName = "General news",
            rssUrl = "https://news.google.com/rss?hl=en-US&gl=US&ceid=US:en",
            fallbackImage = "https://images.unsplash.com/photo-1504711434969-e33886168f5c?w=800&auto=format&fit=crop&q=80"
        )
    )

    suspend fun fetchAllRealtimeArticles(): List<ArticleEntity> = withContext(Dispatchers.IO) {
        coroutineScope {
            val deferredList = FEEDS.map { feed ->
                async {
                    fetchCategoryFeed(feed)
                }
            }
            deferredList.awaitAll().flatten()
        }
    }

    private fun fetchCategoryFeed(feed: CategoryFeed): List<ArticleEntity> {
        val articles = mutableListOf<ArticleEntity>()
        try {
            val request = Request.Builder()
                .url(feed.rssUrl)
                .header("User-Agent", "Mozilla/5.0 (Android; Mobile; rv:120.0) DailyBrief/1.0")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.w(TAG, "Failed to fetch RSS for ${feed.categoryName}: HTTP ${response.code}")
                    return emptyList()
                }

                val xmlBody = response.body?.string() ?: return emptyList()
                val parsedItems = parseRssItems(xmlBody, feed.categoryName, feed.fallbackImage)
                articles.addAll(parsedItems.take(3)) // Take top 3 freshest per category
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error fetching live RSS for ${feed.categoryName}: ${e.message}")
        }
        return articles
    }

    private fun parseRssItems(xml: String, categoryName: String, fallbackImage: String): List<ArticleEntity> {
        val list = mutableListOf<ArticleEntity>()
        val itemPattern = Pattern.compile("<item>([\\s\\S]*?)</item>", Pattern.CASE_INSENSITIVE)
        val matcher = itemPattern.matcher(xml)

        while (matcher.find()) {
            val itemBlock = matcher.group(1) ?: continue

            val rawTitle = extractTag(itemBlock, "title")
            val link = extractTag(itemBlock, "link")
            val pubDate = extractTag(itemBlock, "pubDate")
            val rawDesc = extractTag(itemBlock, "description")
            val rawSource = extractTag(itemBlock, "source")

            if (rawTitle.isBlank() || link.isBlank()) continue

            // Separate headline from source suffix if needed (e.g. "Title - Source")
            var headline = cleanHtml(rawTitle)
            var source = cleanHtml(rawSource)
            if (source.isBlank() && headline.contains(" - ")) {
                val parts = headline.split(" - ")
                source = parts.last().trim()
                headline = parts.dropLast(1).joinToString(" - ").trim()
            }
            if (source.isBlank()) {
                source = "Real-Time News Wire"
            }

            // Extract plain text snippet from description
            val snippet = cleanHtml(rawDesc).replace(Regex("<[^>]*>"), " ").replace(Regex("\\s+"), " ").trim()

            // Construct 3-4 bullet points Executive AI Summary
            val summary = generateExecutiveSummary(headline, snippet, source)

            val parsedDate = DateTimeUtil.parseDate(pubDate)
            val articleTime = parsedDate?.time ?: System.currentTimeMillis()

            list.add(
                ArticleEntity(
                    id = 0, // auto-generated
                    category = categoryName,
                    headline = headline,
                    summary = summary,
                    originalContent = if (snippet.isNotBlank()) snippet else headline,
                    imageUrl = fallbackImage,
                    source = source,
                    sourceUrl = link,
                    publishedAt = pubDate.ifBlank { DateTimeUtil.formatFullDateTime(ArticleEntity(0, "", "", "", publishedAt = "", isCached = false, createdAt = articleTime)) },
                    isCached = false,
                    createdAt = articleTime
                )
            )
        }
        return list
    }

    private fun extractTag(block: String, tagName: String): String {
        val pattern = Pattern.compile("<$tagName(?:[^>]*)>(?:<!\\[CDATA\\[)?([\\s\\S]*?)(?:\\]\\]>)?</$tagName>", Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(block)
        return if (matcher.find()) {
            matcher.group(1)?.trim() ?: ""
        } else ""
    }

    private fun cleanHtml(text: String): String {
        return text
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .replace("&apos;", "'")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&nbsp;", " ")
            .trim()
    }

    private fun generateExecutiveSummary(headline: String, snippet: String, source: String): String {
        val bullets = mutableListOf<String>()
        bullets.add("• Live coverage: $headline")

        if (snippet.isNotBlank() && snippet.length > 25) {
            val sentences = snippet.split(Regex("(?<=[.!?])\\s+")).filter { it.isNotBlank() }
            if (sentences.isNotEmpty()) {
                bullets.add("• Key context: ${sentences[0].take(180).trim()}")
            }
            if (sentences.size > 1) {
                bullets.add("• Details: ${sentences[1].take(180).trim()}")
            }
        }

        bullets.add("• Verified real-time reporting via $source.")
        bullets.add("• Fast executive summary compiled directly from live news feeds.")

        return bullets.joinToString("\n")
    }
}
