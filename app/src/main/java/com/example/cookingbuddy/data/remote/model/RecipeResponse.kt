package com.example.cookingbuddy.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RecipeResponse(
    val id: Int = 0,
    val title: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val duration: String = "30 min",
    val imageUrl: String = ""
)