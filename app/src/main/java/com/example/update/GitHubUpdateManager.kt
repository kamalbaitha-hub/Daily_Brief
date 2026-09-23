package com.example.update

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import com.example.MainActivity
import com.example.R
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GitHubRelease(
    @Json(name = "tag_name") val tagName: String? = "",
    @Json(name = "name") val name: String? = "",
    @Json(name = "body") val body: String? = "",
    @Json(name = "html_url") val htmlUrl: String? = "",
    @Json(name = "published_at") val publishedAt: String? = "",
    @Json(name = "assets") val assets: List<GitHubAsset>? = emptyList()
)

@JsonClass(generateAdapter = true)
data class GitHubAsset(
    @Json(name = "name") val name: String? = "",
    @Json(name = "size") val size: Long? = 0,
    @Json(name = "browser_download_url") val browserDownloadUrl: String? = "",
    @Json(name = "content_type") val contentType: String? = ""
)

interface GitHubApiService {
    @GET("repos/{owner}/{repo}/releases/latest")
    suspend fun getLatestRelease(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Header("User-Agent") userAgent: String = "DailyBrief-AndroidApp"
    ): GitHubRelease
}

data class UpdateInfo(
    val hasUpdate: Boolean,
    val latestVersion: String,
    val currentVersion: String,
    val releaseNotes: String,
    val downloadUrl: String,
    val releasePageUrl: String
)

object GitHubUpdateManager {
    private const val TAG = "GitHubUpdateManager"
    const val UPDATE_CHANNEL_ID = "app_updates_channel"
    const val UPDATE_NOTIFICATION_ID = 9001

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val api: GitHubApiService = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(GitHubApiService::class.java)

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "App Updates"
            val descriptionText = "Notifications when a new version of Daily Brief is available"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(UPDATE_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    suspend fun checkForUpdates(
        context: Context,
        repoOwner: String,
        repoName: String
    ): Result<UpdateInfo> = withContext(Dispatchers.IO) {
        val cleanOwner = repoOwner.trim()
        val cleanRepo = repoName.trim()

        val packageInfo = try {
            context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: Exception) {
            null
        }
        val currentVersion = packageInfo?.versionName ?: "1.0"

        if (cleanOwner.isBlank() || cleanRepo.isBlank()) {
            return@withContext Result.success(
                UpdateInfo(
                    hasUpdate = false,
                    latestVersion = currentVersion,
                    currentVersion = currentVersion,
                    releaseNotes = "GitHub repository not configured.",
                    downloadUrl = "",
                    releasePageUrl = ""
                )
            )
        }

        try {
            val release = api.getLatestRelease(cleanOwner, cleanRepo)
            val latestTag = (release.tagName ?: release.name ?: "").removePrefix("v").trim()

            val isNewer = compareVersions(latestTag, currentVersion) > 0

            val apkAsset = release.assets?.firstOrNull {
                it.name?.endsWith(".apk", ignoreCase = true) == true
            }

            val downloadUrl = apkAsset?.browserDownloadUrl ?: release.htmlUrl ?: ""

            val info = UpdateInfo(
                hasUpdate = isNewer,
                latestVersion = latestTag.ifBlank { "v$latestTag" },
                currentVersion = currentVersion,
                releaseNotes = release.body ?: "New release available on GitHub",
                downloadUrl = downloadUrl,
                releasePageUrl = release.htmlUrl ?: ""
            )

            if (isNewer) {
                notifyUpdateAvailable(context, info)
            }

            Result.success(info)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                // HTTP 404 is normal when no releases have been published yet or repo is new
                Log.d(TAG, "No GitHub releases found for $cleanOwner/$cleanRepo (HTTP 404). Current version is up to date.")
                Result.success(
                    UpdateInfo(
                        hasUpdate = false,
                        latestVersion = currentVersion,
                        currentVersion = currentVersion,
                        releaseNotes = "No releases found on GitHub. You are on the latest version.",
                        downloadUrl = "",
                        releasePageUrl = "https://github.com/$cleanOwner/$cleanRepo"
                    )
                )
            } else if (e.code() == 403) {
                Log.w(TAG, "GitHub API rate limit exceeded or access forbidden: ${e.message}")
                Result.failure(Exception("GitHub API rate limit reached. Please try again later."))
            } else {
                Log.w(TAG, "GitHub API returned HTTP ${e.code()}: ${e.message()}")
                Result.failure(Exception("GitHub returned HTTP ${e.code()}"))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Failed to check for updates: ${e.message}")
            Result.failure(e)
        }
    }

    private fun compareVersions(v1: String, v2: String): Int {
        val parts1 = v1.split(".").mapNotNull { it.filter { ch -> ch.isDigit() }.toIntOrNull() }
        val parts2 = v2.split(".").mapNotNull { it.filter { ch -> ch.isDigit() }.toIntOrNull() }
        val maxLen = maxOf(parts1.size, parts2.size)
        for (i in 0 until maxLen) {
            val p1 = parts1.getOrElse(i) { 0 }
            val p2 = parts2.getOrElse(i) { 0 }
            if (p1 != p2) {
                return p1.compareTo(p2)
            }
        }
        return 0
    }

    fun notifyUpdateAvailable(context: Context, updateInfo: UpdateInfo) {
        createNotificationChannel(context)
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("OPEN_UPDATE_PROMPT", true)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            UPDATE_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, UPDATE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Daily Brief Update Available")
            .setContentText("Version ${updateInfo.latestVersion} is ready to install.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("A new update (${updateInfo.latestVersion}) is available. Tap to review release notes and install.")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        try {
            NotificationManagerCompat.from(context).notify(UPDATE_NOTIFICATION_ID, builder.build())
        } catch (e: SecurityException) {
            Log.w(TAG, "Notification permission denied: ${e.message}")
        }
    }

    suspend fun downloadAndInstallApk(
        context: Context,
        apkUrl: String,
        versionTag: String,
        onProgress: (Int) -> Unit
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(apkUrl).build()
            val response = okHttpClient.newCall(request).execute()

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("HTTP ${response.code}: Failed to download APK"))
            }

            val body = response.body ?: return@withContext Result.failure(Exception("Empty download body"))
            val contentLength = body.contentLength()

            val updateDir = File(context.cacheDir, "updates").apply { mkdirs() }
            val apkFile = File(updateDir, "daily_brief_update_${versionTag}.apk")

            body.byteStream().use { input ->
                FileOutputStream(apkFile).use { output ->
                    val buffer = ByteArray(8 * 1024)
                    var bytesRead: Int
                    var totalRead: Long = 0

                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        totalRead += bytesRead
                        if (contentLength > 0) {
                            val progress = ((totalRead * 100) / contentLength).toInt()
                            onProgress(progress)
                        }
                    }
                    output.flush()
                }
            }

            // Launch package installer on Main thread
            withContext(Dispatchers.Main) {
                installApk(context, apkFile)
            }

            Result.success(apkFile)
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading APK: ${e.message}", e)
            Result.failure(e)
        }
    }

    fun installApk(context: Context, apkFile: File) {
        try {
            val contentUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                apkFile
            )

            val installIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(contentUri, "application/vnd.android.package-archive")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(installIntent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start package installer: ${e.message}", e)
        }
    }
}
