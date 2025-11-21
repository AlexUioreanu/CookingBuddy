package com.example.cookingbuddy.data.utils

import com.example.cookingbuddy.data.local.model.RecipeEntity
import com.example.cookingbuddy.data.remote.model.RecipeResponse
import com.example.cookingbuddy.domain.model.Recipe

fun RecipeResponse.entityToDomain(): Recipe {
    return Recipe(
        id = this.id,
        title = this.title,
        ingredients = this.ingredients,
        instructions = this.instructions,
        imageUrl = this.imageUrl,
        duration = this.duration,
    )
}

fun RecipeEntity.responseToDomain(): Recipe {
    return Recipe(
        id = this.id,
        title = this.title,
        ingredients = this.ingredients,
        instructions = this.instructions,
        imageUrl = this.imageUrl,
        duration = this.duration,
    )
}

fun Recipe.toData(): RecipeEntity {
    return RecipeEntity(
        id = this.id,
        title = this.title,
        ingredients = this.ingredients,
        instructions = this.instructions,
        imageUrl = this.imageUrl,
        duration = this.duration,
        createdAt = System.currentTimeMillis()
    )
}

fun List<RecipeEntity>.entitiesToDomain(): List<Recipe> {
    return this.map { it.responseToDomain() }
}

fun List<RecipeResponse>.responsesToDomain(): List<Recipe> {
    return this.map { it.entityToDomain() }
}
