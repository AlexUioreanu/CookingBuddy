package com.example.cookingbuddy.data.remote

import android.util.Log
import com.example.cookingbuddy.BuildConfig
import com.example.cookingbuddy.data.remote.model.FalNanoRequest
import com.example.cookingbuddy.data.remote.model.FalResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

interface ImageDataSource {
    suspend fun generateImage(title: String, ingredients: List<String>): String
}

class FalImageDataSource(
    private val okHttpClient: OkHttpClient,
    private val jsonParser: Json
) : ImageDataSource {

    override suspend fun generateImage(title: String, ingredients: List<String>): String {
        val ingredientsString = ingredients.take(2).joinToString(", ")
        val richPrompt = "Food photo, $title, $ingredientsString, 4k"

        val falRequest = FalNanoRequest(
            prompt = richPrompt,
            aspectRatio = "1:1",
            syncMode = true,
            outputFormat = "jpeg"
        )

        val requestBody = jsonParser.encodeToString(falRequest)
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(BuildConfig.FAL_API_BASE_URL)
            .addHeader("Authorization", "Key ${BuildConfig.FAL_API_KEY}")
            .post(requestBody)
            .build()

        return try {
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e("FalAI", "Failed: ${response.code}")
                    return BuildConfig.LOREM_FLICKR_URL
                }
                val responseBody = response.body?.string() ?: return ""
                val falResponse = jsonParser.decodeFromString<FalResponse>(responseBody)
                falResponse.images.firstOrNull()?.url ?: ""
            }
        } catch (e: Exception) {
            Log.e("FalAI", "Network error", e)
            ""
        }
    }
}