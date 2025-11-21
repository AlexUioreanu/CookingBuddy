package com.example.cookingbuddy.domain.usecase

import com.example.cookingbuddy.data.model.Recipe
import com.example.cookingbuddy.data.repository.RecipeRepository

class GetRecipesUseCase(private val recipeRepository: RecipeRepository) {
    suspend operator fun invoke(query: String): List<Recipe> {
        return recipeRepository.getRecipes(query)
    }
}