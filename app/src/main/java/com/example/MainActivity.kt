package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notification.NotificationHelper
import com.example.ui.DailyBriefViewModel
import com.example.ui.components.ServerConfigDialog
import com.example.ui.components.UpdateDialog
import com.example.ui.screens.CategoryDetailScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.update.GitHubUpdateManager

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Notification Channels & 8 AM Schedule
        NotificationHelper.createNotificationChannel(this)
        NotificationHelper.scheduleDaily8AmNotification(this)
        GitHubUpdateManager.createNotificationChannel(this)

        setContent {
            DailyBriefApp()
        }
    }
}

@Composable
fun DailyBriefApp(
    viewModel: DailyBriefViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showServerDialog by remember { mutableStateOf(false) }

    // Request Notification permission for Android 13+ (TIRAMISU)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        val activity = context as? android.app.Activity
        val shouldOpenUpdate = activity?.intent?.getBooleanExtra("OPEN_UPDATE_PROMPT", false) == true
        if (shouldOpenUpdate) {
            viewModel.showUpdateDialogManual()
        } else {
            // Check for updates silently on startup
            viewModel.checkForUpdates(userInitiated = false)
        }
    }

    val isDark = uiState.isDarkTheme ?: isSystemInDarkTheme()

    MyApplicationTheme(darkTheme = isDark) {
        Surface(modifier = Modifier.fillMaxSize()) {
            if (uiState.selectedArticleForDetail != null) {
                BackHandler {
                    viewModel.closeDetail()
                }
                CategoryDetailScreen(
                    article = uiState.selectedArticleForDetail!!,
                    onBack = { viewModel.closeDetail() }
                )
            } else {
                HomeScreen(
                    uiState = uiState,
                    onRefresh = { viewModel.refreshData() },
                    onCategorySelected = { viewModel.selectCategory(it) },
                    onArticleClick = { viewModel.openDetail(it) },
                    onToggleTheme = { viewModel.toggleTheme() },
                    onOpenSettings = { showServerDialog = true },
                    onCheckUpdates = { viewModel.showUpdateDialogManual() },
                    onDismissStatus = { viewModel.clearStatusMessage() }
                )
            }

            // Update Dialog when an update is available and requested
            if (uiState.showUpdateDialog && uiState.updateInfo != null) {
                UpdateDialog(
                    updateInfo = uiState.updateInfo!!,
                    isDownloading = uiState.isDownloadingUpdate,
                    downloadProgress = uiState.downloadProgress,
                    onConfirmUpdate = {
                        viewModel.downloadAndInstallUpdate()
                    },
                    onDismiss = {
                        viewModel.dismissUpdateDialog()
                    }
                )
            }

            if (showServerDialog) {
                ServerConfigDialog(
                    currentUrl = uiState.serverUrl,
                    githubOwner = uiState.githubOwner,
                    githubRepo = uiState.githubRepo,
                    isCheckingUpdate = uiState.isCheckingUpdate,
                    onSaveSettings = { url, owner, repo ->
                        viewModel.updateServerUrl(url)
                        viewModel.setGitHubRepoConfig(owner, repo)
                    },
                    onCheckUpdateNow = {
                        viewModel.checkForUpdates(userInitiated = true)
                    },
                    onTriggerPipeline = { viewModel.triggerBackendPipelineRefresh() },
                    onTriggerNotification = {
                        viewModel.triggerTestNotification(context)
                    },
                    onDismiss = { showServerDialog = false }
                )
            }
        }
    }
}
