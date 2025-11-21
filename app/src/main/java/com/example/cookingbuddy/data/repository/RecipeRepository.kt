package com.example.cookingbuddy.data.repository

import com.example.cookingbuddy.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun getRecipes(query: String): Result<List<Recipe>>
    suspend fun addFavorite(recipe: Recipe)
    fun getAllFavoriteRecipesFlow(): Flow<List<Recipe>>
    suspend fun removeFavorite(id: Int)
}
