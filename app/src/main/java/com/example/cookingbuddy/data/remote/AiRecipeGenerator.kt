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
    You are a high-performance cooking assistant API. Your ONLY job is to generate recipes for SOLID FOOD dishes.
    
    Analyze the user's query: "$query"
    
    ---
    PHASE 1: SAFETY & EXCLUSIONS
    1. Do NOT generate recipes for drinks, beverages, cocktails, smoothies, coffees, or liquids (e.g., "Hugo", "Mojito", "Latte", "Lemonade").
    2. If the query is for a drink or non-food item, YOU MUST RETURN an empty JSON array: [].
    
    ---
    PHASE 2: CONTENT REQUIREMENTS
    1. **Language:** Detect the language of the user's query. All JSON values (title, ingredients, instructions) MUST be in that specific language.
    2. **Quantity:** Do NOT limit the number of results to a small default. Generate as many relevant and distinct recipes as possible for the query.
    3. **Duration Format:** You must strictly use the format "H h M min." or "M min" ending with a dot.
       - Example: "1 h 20 min."
       - Example: "45 min."
    
    ---
    PHASE 3: OUTPUT SCHEMA
    Strictly return a JSON array matching this schema: 
    [{"title": "String", "ingredients": ["String"], "instructions": ["String"], "duration": "String", "imageUrl": ""}]
    
    Return ONLY the raw JSON. Do not include markdown formatting (no ```json tags).
    """.trimIndent()
    }
}