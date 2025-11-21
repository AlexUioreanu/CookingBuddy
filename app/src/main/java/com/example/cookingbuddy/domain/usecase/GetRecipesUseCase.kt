package com.example.cookingbuddy.domain.usecase

import com.example.cookingbuddy.data.repository.RecipeRepository
import com.example.cookingbuddy.domain.model.Recipe

class GetRecipesUseCase(private val recipeRepository: RecipeRepository) {
    suspend operator fun invoke(query: String): Result<List<Recipe>> {
        return recipeRepository.getRecipes(query)
    }
}
