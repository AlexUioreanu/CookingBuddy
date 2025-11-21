package com.example.cookingbuddy.data.remote

import android.util.Log
import com.example.cookingbuddy.data.remote.model.RecipeResponse
import com.example.cookingbuddy.data.utils.extractJsonArray
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import java.util.UUID

class AiRecipeGenerator(
    private val jsonParser: Json,
    private val generativeModel: GenerativeModel,
    private val imageDataSource: ImageDataSource
) {
    suspend fun generateRecipes(query: String): List<RecipeResponse> {
        val prompt = createPrompt(query)

        val response = generativeModel.generateContent(prompt)

        val cleanJson = extractJsonArray(
            response.text ?: throw IllegalStateException("Gemini returned empty or null content.")
        )

        val recipes = try {
            jsonParser.decodeFromString<List<RecipeResponse>>(cleanJson)
        } catch (e: Exception) {
            Log.e("Gemini", "Failed to parse recipe JSON", e)
            return emptyList()
        }

        return coroutineScope {
            recipes.map { recipe ->
                async {
                    val aiImageUrl = runCatching {
                        imageDataSource.generateImage(recipe.title, recipe.ingredients)
                    }.getOrElse { "" }

                    recipe.copy(
                        id = UUID.randomUUID().mostSignificantBits.toInt(),
                        imageUrl = aiImageUrl
                    )
                }
            }.awaitAll()
        }
    }

    private fun createPrompt(query: String): String {
        return """
        You are a recipe API. Generate a JSON array of all highly relevant recipes found for: "$query".
        Strict Schema: [{"title": "String", "ingredients": ["String"], "instructions": ["String"], "duration": "String", "imageUrl": ""}]
        Return ONLY the JSON array.
        Format duration example: 1 h 20 min and a '.' at the end of duration
        DO NOT limit the count. Generate as many relevant recipes as possible.
        """.trimIndent()
    }
}