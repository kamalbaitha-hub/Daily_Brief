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

    suspend fun ensureInitialDataLoaded() = withContext(Dispatchers.IO) {
        try {
            val count = articleDao.getArticleCount()
            if (count == 0) {
                Log.d(TAG, "Database empty, inserting initial curated articles...")
                articleDao.insertArticles(InitialData.SEED_ARTICLES)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking/seeding initial data: ${e.message}", e)
        }
    }

    suspend fun refreshFromRemote(): Result<Int> = withContext(Dispatchers.IO) {
        try {
            ensureInitialDataLoaded()

            Log.d(TAG, "Fetching live real-time news briefs...")
            // 1. Fetch live real-time feeds directly over the internet
            val realtimeArticles = try {
                RealtimeNewsFetcher.fetchAllRealtimeArticles()
            } catch (e: Exception) {
                Log.w(TAG, "Direct RSS fetch error: ${e.message}")
                emptyList()
            }

            if (realtimeArticles.isNotEmpty()) {
                // Insert real-time articles into Room DB
                articleDao.insertArticles(realtimeArticles)
                Log.d(TAG, "Inserted ${realtimeArticles.size} real-time live articles into database")
                return@withContext Result.success(realtimeArticles.size)
            }

            // 2. Fallback to backend API if configured
            Log.d(TAG, "Attempting backend service sync...")
            val response = apiService.getDailyBrief()
            val remoteArticles = response.articles ?: emptyList()

            if (remoteArticles.isNotEmpty()) {
                val entities = remoteArticles.mapIndexed { index, dto ->
                    ArticleEntity(
                        id = if (dto.id != null && dto.id > 0) dto.id else 0,
                        category = dto.category?.ifBlank { "General news" } ?: "General news",
                        headline = dto.headline ?: "Headline update",
                        summary = dto.summary ?: "Summary unavailable",
                        originalContent = dto.originalContent ?: "",
                        imageUrl = dto.imageUrl ?: "",
                        source = dto.source ?: "Daily Brief",
                        sourceUrl = dto.sourceUrl ?: "",
                        publishedAt = dto.publishedAt ?: "",
                        isCached = false,
                        createdAt = System.currentTimeMillis()
                    )
                }

                articleDao.insertArticles(entities)
                Log.d(TAG, "Saved ${entities.size} articles to Room")
                Result.success(entities.size)
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
