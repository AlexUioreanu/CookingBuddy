package com.example.cookingbuddy.domain.usecase

import com.example.cookingbuddy.data.repository.RecipeRepository
import com.example.cookingbuddy.domain.model.Recipe

class AddFavoriteUseCase(private val recipeRepository: RecipeRepository) {
    suspend operator fun invoke(recipe: Recipe) {
        recipeRepository.addFavorite(recipe)
    }
}