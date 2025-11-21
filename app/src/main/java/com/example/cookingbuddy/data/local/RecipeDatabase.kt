package com.example.cookingbuddy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cookingbuddy.data.local.model.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}
