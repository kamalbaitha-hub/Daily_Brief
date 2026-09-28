package com.example.data.repository

import android.util.Log
import com.example.data.local.ArticleDao
import com.example.data.local.ArticleEntity
import com.example.data.local.InitialData
import com.example.data.remote.DailyBriefApiService
import com.example.data.remote.NetworkClient
import com.example.data.remote.RealtimeNewsFetcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ArticleRepository(
    private val articleDao: ArticleDao,
    private var apiService: DailyBriefApiService = NetworkClient.createService()
) {
    companion object {
        private const val TAG = "ArticleRepository"
    }

    val allArticles: Flow<List<ArticleEntity>> = articleDao.getAllArticles()

    fun getArticlesByCategory(category: String): Flow<List<ArticleEntity>> =
        articleDao.getArticlesByCategory(category)

    suspend fun getArticleById(id: Int): ArticleEntity? = withContext(Dispatchers.IO) {
        articleDao.getArticleById(id)
    }

    fun updateBaseUrl(newBaseUrl: String) {
        apiService = NetworkClient.createService(newBaseUrl)
    }

    suspend fun purgeArticlesOlderThan24Hours(): Int = withContext(Dispatchers.IO) {
        try {
            val now = System.currentTimeMillis()
            val cutoff = now - com.example.ui.util.DateTimeUtil.TWENTY_FOUR_HOURS_MS
            val all = articleDao.getAllArticlesList()
            val expiredIds = all.filter { article ->
                val timestamp = com.example.ui.util.DateTimeUtil.getArticleTimestamp(article)
                (now - timestamp) > com.example.ui.util.DateTimeUtil.TWENTY_FOUR_HOURS_MS
            }.map { it.id }

            var deleted = 0
            if (expiredIds.isNotEmpty()) {
                deleted = articleDao.deleteArticlesByIds(expiredIds)
                Log.d(TAG, "Purged $deleted articles older than 24 hours from database (found ${expiredIds.size} expired)")
            }
            articleDao.deleteArticlesOlderThan(cutoff)
            deleted
        } catch (e: Exception) {
            Log.e(TAG, "Error purging old articles: ${e.message}", e)
            0
        }
    }

    suspend fun ensureInitialDataLoaded() = withContext(Dispatchers.IO) {
        try {
            purgeArticlesOlderThan24Hours()
            val count = articleDao.getArticleCount()
            if (count == 0) {
                Log.d(TAG, "Database empty, inserting fresh initial curated articles...")
                articleDao.insertArticles(InitialData.getFreshSeedArticles())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking/seeding initial data: ${e.message}", e)
        }
    }

    suspend fun refreshFromRemote(): Result<Int> = withContext(Dispatchers.IO) {
        try {
            purgeArticlesOlderThan24Hours()

            Log.d(TAG, "Fetching live real-time news briefs...")
            // 1. Fetch live real-time feeds directly over the internet
            val realtimeArticles = try {
                RealtimeNewsFetcher.fetchAllRealtimeArticles()
            } catch (e: Exception) {
                Log.w(TAG, "Direct RSS fetch error: ${e.message}")
                emptyList()
            }

            val freshRealtimeArticles = realtimeArticles.filter {
                com.example.ui.util.DateTimeUtil.isArticleWithin24Hours(it)
            }

            if (freshRealtimeArticles.isNotEmpty()) {
                // Insert real-time articles into Room DB
                articleDao.insertArticles(freshRealtimeArticles)
                purgeArticlesOlderThan24Hours()
                Log.d(TAG, "Inserted ${freshRealtimeArticles.size} fresh real-time live articles into database")
                return@withContext Result.success(freshRealtimeArticles.size)
            }

            // 2. Fallback to backend API if configured
            Log.d(TAG, "Attempting backend service sync...")
            val response = apiService.getDailyBrief()
            val remoteArticles = response.articles ?: emptyList()

            if (remoteArticles.isNotEmpty()) {
                val entities = remoteArticles.mapIndexedNotNull { index, dto ->
                    val pubDate = dto.publishedAt ?: ""
                    val parsedTime = com.example.ui.util.DateTimeUtil.parseDate(pubDate)?.time ?: System.currentTimeMillis()
                    val entity = ArticleEntity(
                        id = if (dto.id != null && dto.id > 0) dto.id else 0,
                        category = dto.category?.ifBlank { "General news" } ?: "General news",
                        headline = dto.headline ?: "Headline update",
                        summary = dto.summary ?: "Summary unavailable",
                        originalContent = dto.originalContent ?: "",
                        imageUrl = dto.imageUrl ?: "",
                        source = dto.source ?: "Daily Brief",
                        sourceUrl = dto.sourceUrl ?: "",
                        publishedAt = pubDate,
                        isCached = false,
                        createdAt = parsedTime
                    )
                    if (com.example.ui.util.DateTimeUtil.isArticleWithin24Hours(entity)) entity else null
                }

                if (entities.isNotEmpty()) {
                    articleDao.insertArticles(entities)
                    purgeArticlesOlderThan24Hours()
                    Log.d(TAG, "Saved ${entities.size} fresh articles to Room")
                    Result.success(entities.size)
                } else {
                    Result.success(0)
                }
            } else {
                Result.success(0)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Online sync failed (${e.message}), using Room offline cache", e)
            Result.failure(e)
        }
    }

    suspend fun triggerRemotePipelineRefresh(): Result<Int> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Requesting backend to re-fetch and summarize...")
            val response = apiService.triggerRefresh()
            val remoteArticles = response.articles ?: emptyList()
            if (remoteArticles.isNotEmpty()) {
                val entities = remoteArticles.mapIndexed { index, dto ->
                    ArticleEntity(
                        id = if (dto.id != null && dto.id > 0) dto.id else (index + 1),
                        category = dto.category?.ifBlank { "General news" } ?: "General news",
                        headline = dto.headline ?: "Headline update",
                        summary = dto.summary ?: "Summary unavailable",
                        originalContent = dto.originalContent ?: "",
                        imageUrl = dto.imageUrl ?: "",
                        source = dto.source ?: "Daily Brief",
                        sourceUrl = dto.sourceUrl ?: "",
                        publishedAt = dto.publishedAt ?: ""
                    )
                }
                articleDao.insertArticles(entities)
                Result.success(entities.size)
            } else {
                refreshFromRemote()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Trigger refresh failed: ${e.message}")
            Result.failure(e)
        }
    }
}
