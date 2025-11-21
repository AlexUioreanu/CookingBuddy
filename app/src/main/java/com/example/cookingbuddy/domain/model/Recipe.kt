package com.example.cookingbuddy.domain.model

data class Recipe(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val duration: String
)