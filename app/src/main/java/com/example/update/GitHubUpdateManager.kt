package com.example.update

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
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
import java.io.FileInputStream
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

@JsonClass(generateAdapter = true)
data class ServerUpdateDto(
    @Json(name = "hasUpdate") val hasUpdate: Boolean? = false,
    @Json(name = "latestVersion") val latestVersion: String? = "",
    @Json(name = "versionCode") val versionCode: Int? = 0,
    @Json(name = "tagName") val tagName: String? = "",
    @Json(name = "releaseNotes") val releaseNotes: String? = "",
    @Json(name = "downloadUrl") val downloadUrl: String? = "",
    @Json(name = "releasePageUrl") val releasePageUrl: String? = ""
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
        repoName: String,
        serverUrl: String = ""
    ): Result<UpdateInfo> = withContext(Dispatchers.IO) {
        val cleanOwner = repoOwner.trim()
        val cleanRepo = repoName.trim()

        val packageInfo = try {
            context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: Exception) {
            null
        }
        val currentVersion = com.example.BuildConfig.VERSION_NAME.ifBlank {
            packageInfo?.versionName ?: "1.4.0"
        }

        // 1. Check primary & fallback backend server endpoints and raw GitHub version.json
        val candidateUrls = listOfNotNull(
            serverUrl.takeIf { it.isNotBlank() },
            "https://raw.githubusercontent.com/$cleanOwner/$cleanRepo/main/public/version.json",
            "https://ais-pre-zkymhrkneugz34madqd3s3-951298028561.asia-east1.run.app",
            "http://10.0.2.2:3000",
            "http://localhost:3000"
        ).distinct()

        for (candidate in candidateUrls) {
            try {
                val cleanBase = candidate.trim().removeSuffix("/")
                val reqUrl = if (cleanBase.endsWith(".json")) cleanBase else "$cleanBase/api/updates/latest"
                val req = Request.Builder()
                    .url(reqUrl)
                    .build()
                val resp = okHttpClient.newCall(req).execute()
                if (resp.isSuccessful) {
                    val body = resp.body?.string()
                    if (!body.isNullOrBlank()) {
                        val serverUpdate = moshi.adapter(ServerUpdateDto::class.java).fromJson(body)
                        if (serverUpdate != null) {
                            val sVer = (serverUpdate.latestVersion ?: serverUpdate.tagName ?: "").removePrefix("v").trim()
                            if (compareVersions(sVer, currentVersion) > 0) {
                                val fullDownload = if (serverUpdate.downloadUrl?.endsWith(".apk", ignoreCase = true) == true) {
                                    if (serverUpdate.downloadUrl.startsWith("http")) serverUpdate.downloadUrl else "$cleanBase${serverUpdate.downloadUrl}"
                                } else {
                                    "https://ais-pre-zkymhrkneugz34madqd3s3-951298028561.asia-east1.run.app/downloads/DailyBrief-latest.apk"
                                }
                                val info = UpdateInfo(
                                    hasUpdate = true,
                                    latestVersion = sVer.ifBlank { "v$sVer" },
                                    currentVersion = currentVersion,
                                    releaseNotes = serverUpdate.releaseNotes ?: "New version $sVer available.",
                                    downloadUrl = fullDownload,
                                    releasePageUrl = serverUpdate.releasePageUrl ?: ""
                                )
                                handleNotification(context, sVer, true, info)
                                return@withContext Result.success(info)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Server update candidate $candidate check failed: ${e.message}")
            }
        }

        // 2. Check GitHub Releases if repo is configured
        if (cleanOwner.isBlank() || cleanRepo.isBlank()) {
            return@withContext Result.success(
                UpdateInfo(
                    hasUpdate = false,
                    latestVersion = currentVersion,
                    currentVersion = currentVersion,
                    releaseNotes = "You are on the latest version ($currentVersion).",
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

            val downloadUrl = apkAsset?.browserDownloadUrl
                ?: "https://ais-pre-zkymhrkneugz34madqd3s3-951298028561.asia-east1.run.app/downloads/DailyBrief-latest.apk"

            val info = UpdateInfo(
                hasUpdate = isNewer,
                latestVersion = latestTag.ifBlank { "v$latestTag" },
                currentVersion = currentVersion,
                releaseNotes = release.body ?: "New release available on GitHub",
                downloadUrl = downloadUrl,
                releasePageUrl = release.htmlUrl ?: ""
            )

            handleNotification(context, latestTag, isNewer, info)

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

    private fun handleNotification(context: Context, latestTag: String, isNewer: Boolean, info: UpdateInfo) {
        if (isNewer) {
            notifyUpdateAvailable(context, info)
        } else {
            try {
                NotificationManagerCompat.from(context).cancel(UPDATE_NOTIFICATION_ID)
            } catch (e: Exception) {}
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

        val iconRes = try {
            android.R.drawable.stat_sys_download_done
        } catch (e: Exception) {
            R.drawable.ic_launcher_foreground
        }

        val builder = NotificationCompat.Builder(context, UPDATE_CHANNEL_ID)
            .setSmallIcon(iconRes)
            .setContentTitle("Daily Brief Update Available")
            .setContentText("Version ${updateInfo.latestVersion} is ready to install.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("A new update (${updateInfo.latestVersion}) is available. Tap to review release notes and install.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        try {
            NotificationManagerCompat.from(context).notify(UPDATE_NOTIFICATION_ID, builder.build())
            Log.d(TAG, "Successfully posted update notification for ${updateInfo.latestVersion}")
        } catch (e: SecurityException) {
            Log.w(TAG, "Notification permission denied: ${e.message}")
        } catch (e: Exception) {
            Log.w(TAG, "Failed to display update notification: ${e.message}")
        }
    }

    suspend fun downloadAndInstallApk(
        context: Context,
        apkUrl: String,
        versionTag: String,
        autoLaunchInstaller: Boolean = true,
        onProgress: (Int) -> Unit
    ): Result<File> = withContext(Dispatchers.IO) {
        val cleanTag = versionTag.removePrefix("v").trim()
        val candidateUrls = listOfNotNull(
            apkUrl.takeIf { it.endsWith(".apk", ignoreCase = true) },
            "https://ais-pre-zkymhrkneugz34madqd3s3-951298028561.asia-east1.run.app/downloads/DailyBrief-latest.apk",
            "https://ais-pre-zkymhrkneugz34madqd3s3-951298028561.asia-east1.run.app/downloads/DailyBrief-v${cleanTag}.apk",
            "https://github.com/kamalbaitha-hub/Daily_Brief/releases/download/v${cleanTag}/DailyBrief-v${cleanTag}.apk",
            apkUrl.takeIf { !it.contains("/releases", ignoreCase = true) }
        ).distinct()

        var lastError: Exception? = null

        for (targetUrl in candidateUrls) {
            try {
                Log.d(TAG, "Attempting APK download from: $targetUrl")
                val request = Request.Builder().url(targetUrl).build()
                val response = okHttpClient.newCall(request).execute()

                if (!response.isSuccessful) {
                    lastError = Exception("HTTP ${response.code} downloading from $targetUrl")
                    continue
                }

                val contentType = response.header("Content-Type", "") ?: ""
                if (contentType.contains("text/html", ignoreCase = true)) {
                    Log.w(TAG, "Skipping $targetUrl because server returned HTML instead of an APK ($contentType)")
                    lastError = Exception("Server returned a webpage instead of an APK binary")
                    continue
                }

                val body = response.body ?: continue
                val contentLength = body.contentLength()

                // Save into external files directory or cacheDir
                val updateDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.cacheDir, "updates").apply { mkdirs() }
                val apkFile = File(updateDir, "DailyBrief-v${cleanTag}.apk")

                body.byteStream().use { input ->
                    FileOutputStream(apkFile).use { output ->
                        val buffer = ByteArray(16 * 1024)
                        var bytesRead: Int
                        var totalRead: Long = 0

                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                            totalRead += bytesRead
                            if (contentLength > 0) {
                                val progress = ((totalRead * 100) / contentLength).toInt().coerceIn(0, 100)
                                onProgress(progress)
                            }
                        }
                        output.flush()
                    }
                }

                // Verify file size
                if (apkFile.length() < 500_000) {
                    Log.w(TAG, "File too small (${apkFile.length()} bytes) to be valid APK")
                    apkFile.delete()
                    lastError = Exception("Downloaded file is too small to be a valid APK (${apkFile.length()} bytes)")
                    continue
                }

                // Verify ZIP magic header (0x50, 0x4B, 0x03, 0x04)
                val isZip = FileInputStream(apkFile).use { fis ->
                    val header = ByteArray(4)
                    val read = fis.read(header)
                    read == 4 && header[0] == 0x50.toByte() && header[1] == 0x4B.toByte() && header[2] == 0x03.toByte() && header[3] == 0x04.toByte()
                }

                if (!isZip) {
                    Log.w(TAG, "Downloaded file does not have valid APK/ZIP magic bytes")
                    apkFile.delete()
                    lastError = Exception("Downloaded file is not a valid APK package (not a ZIP archive)")
                    continue
                }

                // Verify package with PackageManager
                val archiveInfo = context.packageManager.getPackageArchiveInfo(apkFile.absolutePath, 0)
                if (archiveInfo == null) {
                    Log.w(TAG, "PackageManager failed to parse downloaded APK file")
                    apkFile.delete()
                    lastError = Exception("Android system could not parse the downloaded APK file. File may be corrupted.")
                    continue
                }

                Log.i(TAG, "Valid APK verified! Package: ${archiveInfo.packageName}, Version: ${archiveInfo.versionName}")

                // Backup to public Downloads folder so it persists even if old app is uninstalled
                copyToPublicDownloads(apkFile, "DailyBrief-v${cleanTag}.apk")

                onProgress(100)

                if (autoLaunchInstaller) {
                    withContext(Dispatchers.Main) {
                        installApk(context, apkFile)
                    }
                }

                return@withContext Result.success(apkFile)
            } catch (e: Exception) {
                Log.e(TAG, "Error trying $targetUrl: ${e.message}")
                lastError = e
            }
        }

        Result.failure(lastError ?: Exception("Failed to download a valid APK file"))
    }

    fun copyToPublicDownloads(sourceFile: File, destinationFileName: String): File? {
        return try {
            val publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (publicDir.exists() || publicDir.mkdirs()) {
                val destFile = File(publicDir, destinationFileName)
                sourceFile.copyTo(destFile, overwrite = true)
                Log.d(TAG, "Successfully copied APK to public Downloads: ${destFile.absolutePath}")
                destFile
            } else null
        } catch (e: Exception) {
            Log.w(TAG, "Failed to copy APK to public Downloads: ${e.message}")
            null
        }
    }

    fun cleanReinstallApk(context: Context, apkFile: File, versionTag: String) {
        try {
            // 1. Ensure file is also in public Downloads folder so it stays after uninstall
            val publicApk = copyToPublicDownloads(apkFile, "DailyBrief-v${versionTag}.apk") ?: apkFile

            // 2. Post a persistent Notification so user can tap it right after uninstalling
            val installUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                publicApk
            )

            val installIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(installUri, "application/vnd.android.package-archive")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                9002,
                installIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, UPDATE_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Daily Brief $versionTag Ready to Install")
                .setContentText("Tap here to complete installation of Daily Brief.")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("The fresh Daily Brief v$versionTag APK is saved in your Downloads. Tap this notification to complete installation after the old app is uninstalled.")
                )
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(context).notify(9002, notification)

            // 3. Launch Android system uninstaller to remove old app and clear signature conflicts
            val uninstallIntent = Intent(Intent.ACTION_DELETE).apply {
                data = Uri.parse("package:${context.packageName}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(uninstallIntent)
        } catch (e: Exception) {
            Log.e(TAG, "Error initiating clean re-install: ${e.message}", e)
        }
    }

    fun installApk(context: Context, apkFile: File) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!context.packageManager.canRequestPackageInstalls()) {
                    Log.i(TAG, "Prompting user for REQUEST_INSTALL_PACKAGES permission")
                    val settingsIntent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                        data = Uri.parse("package:${context.packageName}")
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(settingsIntent)
                    return
                }
            }

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
