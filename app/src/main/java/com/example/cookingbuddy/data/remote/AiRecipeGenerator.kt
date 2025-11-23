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

        val response = try {
            generativeModel.generateContent(prompt)
        } catch (e: Exception) {
            Log.e("Gemini", "Network/API Error", e)
            return emptyList()
        }

        val responseText = response.text ?: return emptyList()
        val cleanJson = extractJsonArray(responseText)

        val recipes = try {
            jsonParser.decodeFromString<List<RecipeResponse>>(cleanJson)
        } catch (e: Exception) {
            Log.e("Gemini", "Failed to parse recipe JSON", e)
            return emptyList()
        }

        if (recipes.isEmpty()) {
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
        You are a strict cooking assistant API. Your ONLY job is to generate recipes for SOLID FOOD dishes.
        
        Analyze the user's query: "$query"
        
        STRICT EXCLUSION RULES:
        1. Do NOT generate recipes for drinks, beverages, cocktails, smoothies, coffees, or liquids (e.g., "Hugo", "Mojito", "Latte", "Lemonade").
        2. If the query is for a drink or non-food item, YOU MUST RETURN an empty JSON array: [].
        3. Do NOT generate metaphorical recipes.
        
        If the query is for a valid food dish, generate a JSON array of relevant recipes.
        
        Strict Schema: [{"title": "String", "ingredients": ["String"], "instructions": ["String"], "duration": "String", "imageUrl": ""}]
        
        Return ONLY the JSON array (or [] if invalid). Do not add markdown formatting.
        """.trimIndent()
    }
}