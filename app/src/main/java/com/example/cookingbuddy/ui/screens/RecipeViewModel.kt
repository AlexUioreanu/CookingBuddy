package com.example.cookingbuddy.ui.screens

import androidx.lifecycle.ViewModel
import com.example.cookingbuddy.R
import com.example.cookingbuddy.domain.model.Recipe
import com.example.cookingbuddy.domain.usecase.AddFavoriteUseCase
import com.example.cookingbuddy.domain.usecase.GetFavoritesUseCase
import com.example.cookingbuddy.domain.usecase.GetRecipesUseCase
import com.example.cookingbuddy.domain.usecase.RemoveFavoriteUseCase
import com.example.cookingbuddy.ui.utils.ResourcesProvider
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

data class RecipeState(
    val query: String = "",
    val selectedRecipe: Recipe? = null,
    val loading: Boolean = true,
    val showFavorites: Boolean = false,
    val recipes: List<Recipe> = emptyList(),
    val favorites: List<Recipe> = emptyList(),
    val favoriteIds: Set<Int> = emptySet()
)

class RecipeViewModel(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getRecipesUseCase: GetRecipesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val resourcesProvider: ResourcesProvider
) : ViewModel(), ContainerHost<RecipeState, RecipeSideEffect> {

    override val container = container<RecipeState, RecipeSideEffect>(RecipeState())

    init {
        observeFavorites()
    }

    private fun observeFavorites() = intent {
        getFavoritesUseCase().collectLatest { favoriteRecipes ->
            reduce {
                state.copy(
                    favorites = favoriteRecipes,
                    loading = false,
                    favoriteIds = favoriteRecipes.map { it.id }.toSet(),
                    showFavorites = state.query.isEmpty() && favoriteRecipes.isNotEmpty()
                )
            }
        }
    }

    fun onQueryChanged(query: String) = intent {
        reduce {
            val updatedRecipes = if (query.isEmpty()) emptyList() else state.recipes
            state.copy(
                query = query,
                showFavorites = query.isEmpty() && state.favorites.isNotEmpty(),
                recipes = updatedRecipes
            )
        }
    }

    fun onSearch() = intent {
        reduce { state.copy(loading = true) }

        getRecipesUseCase(state.query)
            .onSuccess { list ->
                if (list.isEmpty()) {
                    postSideEffect(RecipeSideEffect.Error(resourcesProvider.getString(R.string.error_recipes_not_found)))
                    reduce { state.copy(recipes = emptyList()) }
                } else {
                    reduce { state.copy(recipes = list) }
                }
            }
            .onFailure {
                postSideEffect(RecipeSideEffect.Error(resourcesProvider.getString(R.string.error_recipe_generation_failed)))
            }
        reduce { state.copy(loading = false) }
    }

    fun onRecipeSelected(recipe: Recipe) = intent {
        reduce { state.copy(selectedRecipe = recipe) }
    }

    fun onFavoriteClicked(recipe: Recipe, isFavorite: Boolean) = intent {
        if (isFavorite) {
            removeFavoriteUseCase(recipe.id)
        } else {
            addFavoriteUseCase(recipe)
        }
    }
}

sealed class RecipeSideEffect {
    data class Error(val message: String) : RecipeSideEffect()
}