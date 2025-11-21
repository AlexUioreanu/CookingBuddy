package com.example.cookingbuddy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cookingbuddy.data.local.model.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeEntity: RecipeEntity)

    @Query("SELECT * FROM recipeentity WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: Int): RecipeEntity?

    @Query("SELECT * FROM recipeentity ORDER BY createdAt DESC")
    fun getAllFavoriteRecipesFlow(): Flow<List<RecipeEntity>>

    @Query("DELETE FROM recipeentity WHERE id = :recipeId")
    suspend fun deleteRecipeById(recipeId: Int)
}
