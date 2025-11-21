package com.example.cookingbuddy.data.repository

import com.example.cookingbuddy.data.local.RecipeDao
import com.example.cookingbuddy.data.remote.AiRecipeGenerator
import com.example.cookingbuddy.data.utils.entitiesToDomain
import com.example.cookingbuddy.data.utils.responsesToDomain
import com.example.cookingbuddy.data.utils.toData
import com.example.cookingbuddy.domain.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RecipeRepositoryImpl(
    private val recipeDao: RecipeDao,
    private val aiRecipeGenerator: AiRecipeGenerator
) : RecipeRepository {

    override suspend fun getRecipes(query: String): Result<List<Recipe>> = runCatching {
        withContext(Dispatchers.IO) {
            aiRecipeGenerator.generateRecipes(query).responsesToDomain()
        }
    }

    override suspend fun addFavorite(recipe: Recipe) {
        recipeDao.insertRecipe(recipe.toData())
    }

    override fun getAllFavoriteRecipesFlow(): Flow<List<Recipe>> =
        recipeDao.getAllFavoriteRecipesFlow()
            .map { dataRecipes -> dataRecipes.entitiesToDomain() }

    override suspend fun removeFavorite(id: Int) {
        recipeDao.deleteRecipeById(id)
    }
}