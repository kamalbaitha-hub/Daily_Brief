package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class BriefResponseDto(
    @Json(name = "success") val success: Boolean? = true,
    @Json(name = "count") val count: Int? = 0,
    @Json(name = "lastUpdated") val lastUpdated: String? = null,
    @Json(name = "articles") val articles: List<ArticleDto>? = emptyList()
)

@JsonClass(generateAdapter = true)
data class ArticleDto(
    @Json(name = "id") val id: Int? = 0,
    @Json(name = "category") val category: String? = "",
    @Json(name = "headline") val headline: String? = "",
    @Json(name = "summary") val summary: String? = "",
    @Json(name = "original_content") val originalContent: String? = null,
    @Json(name = "image_url") val imageUrl: String? = null,
    @Json(name = "source") val source: String? = null,
    @Json(name = "source_url") val sourceUrl: String? = null,
    @Json(name = "published_at") val publishedAt: String? = null
)

interface DailyBriefApiService {
    @GET("api/brief")
    suspend fun getDailyBrief(): BriefResponseDto

    @GET("api/brief/{category}")
    suspend fun getBriefByCategory(@Path("category") category: String): BriefResponseDto

    @POST("api/brief/refresh")
    suspend fun triggerRefresh(): BriefResponseDto
}

object NetworkClient {
    // 10.0.2.2 is the standard loopback IP for Android Emulator connecting to host localhost
    const val DEFAULT_EMULATOR_BASE_URL = "http://10.0.2.2:5000/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    fun createService(baseUrl: String = DEFAULT_EMULATOR_BASE_URL): DailyBriefApiService {
        val sanitizedUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
        return Retrofit.Builder()
            .baseUrl(sanitizedUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(DailyBriefApiService::class.java)
    }
}
