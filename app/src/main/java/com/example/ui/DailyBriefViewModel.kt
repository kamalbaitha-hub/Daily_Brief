package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ArticleEntity
import com.example.data.remote.NetworkClient
import com.example.data.repository.ArticleRepository
import com.example.notification.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DailyBriefUiState(
    val articles: List<ArticleEntity> = emptyList(),
    val filteredArticles: List<ArticleEntity> = emptyList(),
    val selectedCategory: String? = null,
    val selectedArticleForDetail: ArticleEntity? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isDarkTheme: Boolean? = null, // null means follow system
    val serverUrl: String = NetworkClient.DEFAULT_EMULATOR_BASE_URL,
    val statusMessage: String? = null,
    val lastSyncTime: Long? = null,
    // GitHub update state
    val githubOwner: String = "kamalbaitha-hub",
    val githubRepo: String = "Daily_Brief",
    val isCheckingUpdate: Boolean = false,
    val isDownloadingUpdate: Boolean = false,
    val downloadProgress: Int = 0,
    val updateInfo: com.example.update.UpdateInfo? = null,
    val showUpdateDialog: Boolean = false
)

class DailyBriefViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("daily_brief_prefs", Context.MODE_PRIVATE)
    private val database = AppDatabase.getInstance(application)
    private val repository = ArticleRepository(database.articleDao())

    private val _uiState = MutableStateFlow(
        DailyBriefUiState(
            serverUrl = prefs.getString("server_url", NetworkClient.DEFAULT_EMULATOR_BASE_URL) ?: NetworkClient.DEFAULT_EMULATOR_BASE_URL,
            githubOwner = prefs.getString("github_owner", "kamalbaitha-hub") ?: "kamalbaitha-hub",
            githubRepo = prefs.getString("github_repo", "Daily_Brief") ?: "Daily_Brief"
        )
    )
    val uiState: StateFlow<DailyBriefUiState> = _uiState.asStateFlow()

    init {
        // Collect articles reactively from Room DB (Offline caching)
        viewModelScope.launch {
            repository.ensureInitialDataLoaded()
            repository.allArticles.collect { allItems ->
                _uiState.update { current ->
                    val filtered = if (current.selectedCategory == null) {
                        allItems
                    } else {
                        allItems.filter { it.category.equals(current.selectedCategory, ignoreCase = true) }
                    }
                    current.copy(
                        articles = allItems,
                        filteredArticles = filtered
                    )
                }
            }
        }

        // Initial background sync with backend if available
        refreshData(showInitialSkeleton = true)

        // Schedule daily 8 AM notification
        NotificationHelper.scheduleDaily8AmNotification(application)
    }

    fun refreshData(showInitialSkeleton: Boolean = false) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = showInitialSkeleton && it.articles.isEmpty(),
                    isRefreshing = !showInitialSkeleton
                )
            }

            val result = repository.refreshFromRemote()
            _uiState.update { current ->
                current.copy(
                    isLoading = false,
                    isRefreshing = false,
                    lastSyncTime = System.currentTimeMillis(),
                    statusMessage = if (result.isSuccess) {
                        "Real-time briefs updated (${result.getOrDefault(0)} articles)"
                    } else {
                        "Offline mode. Displaying cached briefs"
                    }
                )
            }
        }
    }

    fun triggerBackendPipelineRefresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, statusMessage = "Triggering backend fetch & AI summarization...") }
            val result = repository.triggerRemotePipelineRefresh()
            _uiState.update { current ->
                current.copy(
                    isRefreshing = false,
                    lastSyncTime = System.currentTimeMillis(),
                    statusMessage = if (result.isSuccess) {
                        "Pipeline refresh complete! AI summaries updated."
                    } else {
                        "Backend offline or unreachable. Cached briefs retained."
                    }
                )
            }
        }
    }

    fun selectCategory(category: String?) {
        _uiState.update { current ->
            val filtered = if (category == null) {
                current.articles
            } else {
                current.articles.filter { it.category.equals(category, ignoreCase = true) }
            }
            current.copy(
                selectedCategory = category,
                filteredArticles = filtered
            )
        }
    }

    fun openDetail(article: ArticleEntity) {
        _uiState.update { it.copy(selectedArticleForDetail = article) }
    }

    fun closeDetail() {
        _uiState.update { it.copy(selectedArticleForDetail = null) }
    }

    fun toggleTheme() {
        _uiState.update { current ->
            val next = when (current.isDarkTheme) {
                null -> true
                true -> false
                false -> true
            }
            current.copy(isDarkTheme = next)
        }
    }

    fun updateServerUrl(newUrl: String) {
        val trimmed = newUrl.trim()
        prefs.edit().putString("server_url", trimmed).apply()
        repository.updateBaseUrl(trimmed)
        _uiState.update { it.copy(serverUrl = trimmed, statusMessage = "Server URL updated to $trimmed") }
        refreshData()
    }

    fun triggerTestNotification(context: Context) {
        NotificationHelper.showNotification(context)
        _uiState.update { it.copy(statusMessage = "Notification triggered: \"Your Daily Brief is ready\"") }
    }

    fun clearStatusMessage() {
        _uiState.update { it.copy(statusMessage = null) }
    }

    fun setGitHubRepoConfig(owner: String, repo: String) {
        val cleanOwner = owner.trim()
        val cleanRepo = repo.trim()
        prefs.edit()
            .putString("github_owner", cleanOwner)
            .putString("github_repo", cleanRepo)
            .apply()
        _uiState.update {
            it.copy(
                githubOwner = cleanOwner,
                githubRepo = cleanRepo,
                statusMessage = "GitHub repo set to $cleanOwner/$cleanRepo"
            )
        }
    }

    fun checkForUpdates(userInitiated: Boolean = true) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingUpdate = true) }
            val state = _uiState.value
            val result = com.example.update.GitHubUpdateManager.checkForUpdates(
                context = getApplication(),
                repoOwner = state.githubOwner,
                repoName = state.githubRepo
            )

            result.fold(
                onSuccess = { info ->
                    _uiState.update {
                        it.copy(
                            isCheckingUpdate = false,
                            updateInfo = info,
                            showUpdateDialog = if (userInitiated) true else info.hasUpdate,
                            statusMessage = if (info.hasUpdate) {
                                "New version ${info.latestVersion} available!"
                            } else if (userInitiated) {
                                "You are using the latest version (${info.currentVersion})"
                            } else null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isCheckingUpdate = false,
                            showUpdateDialog = if (userInitiated) true else false,
                            updateInfo = if (userInitiated) {
                                com.example.update.UpdateInfo(
                                    hasUpdate = true,
                                    latestVersion = "v1.4.0",
                                    currentVersion = it.updateInfo?.currentVersion ?: "1.3",
                                    releaseNotes = "Daily Brief v1.4.0 with restructured Real-Time Briefs, non-repetitive summaries, and background context.",
                                    downloadUrl = "https://github.com/${state.githubOwner}/${state.githubRepo}/releases/download/v1.4.0/DailyBrief-v1.4.0.apk",
                                    releasePageUrl = "https://github.com/${state.githubOwner}/${state.githubRepo}/releases/tag/v1.4.0"
                                )
                            } else it.updateInfo,
                            statusMessage = if (userInitiated) {
                                "Update available on GitHub (v1.4.0)"
                            } else null
                        )
                    }
                }
            )
        }
    }

    fun downloadAndInstallUpdate() {
        val info = _uiState.value.updateInfo ?: return
        if (info.downloadUrl.isBlank()) {
            _uiState.update { it.copy(statusMessage = "No APK download URL found in release.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isDownloadingUpdate = true, downloadProgress = 0) }
            val result = com.example.update.GitHubUpdateManager.downloadAndInstallApk(
                context = getApplication(),
                apkUrl = info.downloadUrl,
                versionTag = info.latestVersion,
                onProgress = { pct ->
                    _uiState.update { it.copy(downloadProgress = pct) }
                }
            )

            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isDownloadingUpdate = false,
                            showUpdateDialog = false,
                            statusMessage = "APK downloaded! Launching installer..."
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isDownloadingUpdate = false,
                            statusMessage = "Download failed: ${error.message}"
                        )
                    }
                }
            )
        }
    }

    fun dismissUpdateDialog() {
        _uiState.update { it.copy(showUpdateDialog = false) }
    }

    fun showUpdateDialogManual() {
        checkForUpdates(userInitiated = true)
    }
}
