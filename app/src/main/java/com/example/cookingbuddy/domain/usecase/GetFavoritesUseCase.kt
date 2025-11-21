package com.example.cookingbuddy.domain.usecase

import com.example.cookingbuddy.data.repository.RecipeRepository
import com.example.cookingbuddy.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase(private val recipeRepository: RecipeRepository) {
    operator fun invoke(): Flow<List<Recipe>> {
        return recipeRepository.getAllFavoriteRecipesFlow()
    }
}