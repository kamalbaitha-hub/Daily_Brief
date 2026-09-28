package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import com.example.data.local.ArticleEntity
import com.example.ui.DailyBriefUiState
import com.example.ui.components.CategoryCard
import com.example.ui.components.CategoryChipRow
import com.example.ui.components.CATEGORY_RECOMMENDATIONS
import com.example.ui.components.CATEGORY_NEWSPAPERS
import com.example.ui.components.CATEGORY_MAGAZINES
import com.example.ui.components.NewspaperCornerSection
import com.example.ui.components.MagazineCornerSection
import com.example.ui.components.RecommendationsSection
import com.example.ui.components.SkeletonLoadingCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: DailyBriefUiState,
    onRefresh: () -> Unit,
    onCategorySelected: (String?) -> Unit,
    onArticleClick: (ArticleEntity) -> Unit,
    onToggleTheme: () -> Unit,
    onOpenSettings: () -> Unit,
    onCheckUpdates: () -> Unit,
    onDismissStatus: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullToRefreshState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.statusMessage) {
        uiState.statusMessage?.let {
            snackbarHostState.showSnackbar(it)
            onDismissStatus()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Daily Brief",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = (-0.5).sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "REAL-TIME",
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Real-Time Executive Briefings • Always Live",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    // Manual Refresh Button (Requirement 7)
                    IconButton(
                        onClick = onRefresh,
                        enabled = !uiState.isRefreshing,
                        modifier = Modifier.testTag("manual_refresh_button")
                    ) {
                        if (uiState.isRefreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh briefings"
                            )
                        }
                    }

                    // GitHub Update Check Button
                    IconButton(
                        onClick = onCheckUpdates,
                        modifier = Modifier.testTag("check_updates_button")
                    ) {
                        if (uiState.updateInfo?.hasUpdate == true) {
                            BadgedBox(
                                badge = {
                                    Badge { Text("1") }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SystemUpdate,
                                    contentDescription = "New update available",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.SystemUpdate,
                                contentDescription = "Check for updates"
                            )
                        }
                    }

                    // Theme Toggle (Light / Dark mode)
                    IconButton(
                        onClick = onToggleTheme,
                        modifier = Modifier.testTag("theme_toggle_button")
                    ) {
                        val isDark = uiState.isDarkTheme ?: false
                        Icon(
                            imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Dark/Light Mode"
                        )
                    }

                    // Settings / Server / Scheduler info dialog
                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier.testTag("settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Backend and Notification Settings"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onRefresh,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                modifier = Modifier.testTag("fab_refresh")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Sync latest brief"
                )
            }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            state = pullRefreshState,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Category Filter Chips Row
                CategoryChipRow(
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = onCategorySelected,
                    modifier = Modifier.fillMaxWidth()
                )

                // Main Content List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Subtitle / Count
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when (uiState.selectedCategory) {
                                    CATEGORY_NEWSPAPERS -> "Newspaper Corner"
                                    CATEGORY_MAGAZINES -> "Magazine Corner"
                                    CATEGORY_RECOMMENDATIONS -> "Market Recommendations"
                                    null -> "Today's Top Briefs (${uiState.filteredArticles.size})"
                                    else -> "${uiState.selectedCategory} (${uiState.filteredArticles.size})"
                                },
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = when (uiState.selectedCategory) {
                                    CATEGORY_NEWSPAPERS -> "Daily E-Editions"
                                    CATEGORY_MAGAZINES -> "Popular E-Magazines"
                                    CATEGORY_RECOMMENDATIONS -> "Daily Research Picks"
                                    else -> "Room DB Cached"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // If user clicked Newspaper Corner Chip
                    if (uiState.selectedCategory == CATEGORY_NEWSPAPERS) {
                        item {
                            NewspaperCornerSection(modifier = Modifier.fillMaxWidth())
                        }
                    } else if (uiState.selectedCategory == CATEGORY_MAGAZINES) {
                        // If user clicked Magazine Corner Chip
                        item {
                            MagazineCornerSection(modifier = Modifier.fillMaxWidth())
                        }
                    } else if (uiState.selectedCategory == CATEGORY_RECOMMENDATIONS) {
                        // If user clicked Recommendations Chip
                        item {
                            RecommendationsSection(modifier = Modifier.fillMaxWidth())
                        }
                    } else if (uiState.isLoading) {
                        // Skeleton Loading UI (Requirement 7)
                        items(3) {
                            SkeletonLoadingCard()
                        }
                    } else if (uiState.filteredArticles.isEmpty()) {
                        item {
                            EmptyStateCard(
                                selectedCategory = uiState.selectedCategory,
                                onResetFilter = { onCategorySelected(null) },
                                onRefresh = onRefresh
                            )
                        }
                    } else {
                        val realtimeArticles = uiState.filteredArticles.filter { !it.isCached }
                        val cachedArticles = uiState.filteredArticles.filter { it.isCached }

                        // Top Section: Latest Real-Time Briefs
                        if (realtimeArticles.isNotEmpty()) {
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "LATEST REAL-TIME BRIEFS",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        ),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "${realtimeArticles.size} live briefs",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            itemsIndexed(
                                items = realtimeArticles,
                                key = { _, item -> item.id }
                            ) { index, article ->
                                CategoryCard(
                                    article = article,
                                    onClick = { onArticleClick(article) },
                                    showCoverImage = (index == 0) // Only the top article displays hero cover image
                                )
                            }
                        }

                        // Recommendations Section: Shown on 'All Categories' view between news sections or at top
                        if (uiState.selectedCategory == null) {
                            item {
                                RecommendationsSection(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                )
                            }
                        }

                        // Bottom Section: Cached Briefs
                        if (cachedArticles.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudQueue,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (realtimeArticles.isEmpty()) "CACHED BRIEFS (OFFLINE)" else "CACHED BRIEFS (SAVED ARCHIVE)",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        ),
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "${cachedArticles.size} cached",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }

                            itemsIndexed(
                                items = cachedArticles,
                                key = { _, item -> item.id }
                            ) { index, article ->
                                val shouldShowImage = (realtimeArticles.isEmpty() && index == 0)
                                CategoryCard(
                                    article = article,
                                    onClick = { onArticleClick(article) },
                                    showCoverImage = shouldShowImage
                                )
                            }
                        }
                    }

                    // Bottom Spacing for FAB
                    item {
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateCard(
    selectedCategory: String?,
    onResetFilter: () -> Unit,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No briefings found in this category",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Pull to refresh from backend or view all categories.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (selectedCategory != null) {
                    TextButton(onClick = onResetFilter) {
                        Text("Show All Categories")
                    }
                }
                TextButton(onClick = onRefresh) {
                    Text("Refresh from Server")
                }
            }
        }
    }
}
