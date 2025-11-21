package com.example.cookingbuddy.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val imageUrl: String,
    val duration: String,
    val createdAt: Long = System.currentTimeMillis()
)