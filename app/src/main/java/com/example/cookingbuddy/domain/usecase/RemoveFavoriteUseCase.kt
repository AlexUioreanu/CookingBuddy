package com.example.cookingbuddy.domain.usecase

import com.example.cookingbuddy.data.repository.RecipeRepository

class RemoveFavoriteUseCase(private val recipeRepository: RecipeRepository) {
    suspend operator fun invoke(id: Int) {
        recipeRepository.removeFavorite(id)
    }
}