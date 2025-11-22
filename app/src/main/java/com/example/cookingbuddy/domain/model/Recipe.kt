package com.example.cookingbuddy.domain.model

data class Recipe(
    val isFavorite: Boolean,
    val id: Int,
    val title: String,
    val imageUrl: String,
    val duration: String,
    val ingredients: List<String>,
    val instructions: List<String>
)