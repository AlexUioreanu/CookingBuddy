package com.example.cookingbuddy.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cookingbuddy.R
import com.example.cookingbuddy.domain.model.Recipe
import com.example.cookingbuddy.ui.components.PrimaryButton
import com.example.cookingbuddy.ui.components.RecipeListItem
import com.example.cookingbuddy.ui.components.SearchTextField
import com.example.cookingbuddy.ui.screens.RecipeViewModel
import com.example.cookingbuddy.ui.theme.CookingBuddyTheme
import com.example.cookingbuddy.ui.theme.headlineBigTextStyle
import com.example.cookingbuddy.ui.theme.headlineTextStyle
import com.example.cookingbuddy.ui.utils.RecipeSideEffectHandler

@Composable
fun HomeScreen(
    viewModel: RecipeViewModel,
    onRecipeClick: () -> Unit
) {
    val uiState by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    RecipeSideEffectHandler(viewModel)

    Content(
        query = uiState.query,
        isLoading = uiState.loading,
        showFavorites = uiState.showFavorites,
        recipes = uiState.recipes,
        favorites = uiState.favorites,
        favoriteIds = uiState.favoriteIds,
        onQueryChange = viewModel::onQueryChanged,
        onSearch = {
            focusManager.clearFocus()
            keyboardController?.hide()
            viewModel.onSearch()
        },
        onRecipeClick = { recipe ->
            viewModel.onRecipeSelected(recipe)
            onRecipeClick()
        },
        onToggleFavorite = viewModel::onFavoriteClicked
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    query: String,
    isLoading: Boolean,
    showFavorites: Boolean,
    recipes: List<Recipe>,
    favorites: List<Recipe>,
    favoriteIds: Set<Int>,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onRecipeClick: (Recipe) -> Unit,
    onToggleFavorite: (Recipe, Boolean) -> Unit
) {
    Column(modifier = modifier.padding(16.dp)) {
        SearchTextField(
            value = query,
            onValueChange = onQueryChange,
            onSearch = onSearch
        )

        Spacer(modifier = Modifier.height(4.dp))

        when {
            isLoading -> LoadingView()

            !showFavorites && recipes.isEmpty() -> EmptyStateView()

            else -> RecipeListContent(
                showFavorites = showFavorites,
                favorites = favorites,
                recipes = recipes,
                favoriteIds = favoriteIds,
                onRecipeClick = onRecipeClick,
                onToggleFavorite = onToggleFavorite,
                onSearch = onSearch
            )
        }
    }
}

@Composable
private fun RecipeListContent(
    showFavorites: Boolean,
    favorites: List<Recipe>,
    recipes: List<Recipe>,
    favoriteIds: Set<Int>,
    onRecipeClick: (Recipe) -> Unit,
    onToggleFavorite: (Recipe, Boolean) -> Unit,
    onSearch: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(Modifier.height(16.dp)) }

        if (showFavorites) {
            favoritesSection(
                favorites = favorites,
                onRecipeClick = onRecipeClick,
                onToggleFavorite = onToggleFavorite
            )
        } else {
            suggestionsSection(
                recipes = recipes,
                favoriteIds = favoriteIds,
                onRecipeClick = onRecipeClick,
                onToggleFavorite = onToggleFavorite,
                onNewSuggestions = onSearch
            )
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}

private fun LazyListScope.favoritesSection(
    favorites: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit,
    onToggleFavorite: (Recipe, Boolean) -> Unit
) {
    if (favorites.isNotEmpty()) {
        item {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.title_favorites),
                style = headlineBigTextStyle()
            )
        }
        items(favorites, key = { it.id }) { recipe ->
            RecipeListItem(
                recipe = recipe,
                isFavorite = true,
                onFavoriteClick = { onToggleFavorite(recipe, true) },
                onClick = { onRecipeClick(recipe) }
            )
        }
    }
}

private fun LazyListScope.suggestionsSection(
    recipes: List<Recipe>,
    favoriteIds: Set<Int>,
    onRecipeClick: (Recipe) -> Unit,
    onToggleFavorite: (Recipe, Boolean) -> Unit,
    onNewSuggestions: () -> Unit
) {
    if (recipes.isNotEmpty()) {
        item {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.title_suggested_recipes),
                style = headlineBigTextStyle()
            )
        }

        items(recipes, key = { it.id }) { recipe ->
            val isFavorite = recipe.id in favoriteIds

            RecipeListItem(
                recipe = recipe,
                isFavorite = isFavorite,
                onFavoriteClick = { onToggleFavorite(recipe, isFavorite) },
                onClick = { onRecipeClick(recipe) }
            )
        }

        item { Spacer(Modifier.height(8.dp)) }

        item {
            PrimaryButton(
                text = stringResource(R.string.btn_new_suggestions),
                onClick = onNewSuggestions
            )
        }
    }
}

@Composable
private fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyStateView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.msg_nothing_to_show),
            style = headlineTextStyle()
        )
    }
}

//Preview ----------------------

private val PreviewRecipe = Recipe(
    id = 1,
    title = "Classic Spaghetti Carbonara",
    ingredients = listOf("Pasta", "Eggs", "Pancetta", "Cheese"),
    instructions = listOf("Boil pasta", "Fry pancetta", "Mix eggs and cheese"),
    duration = "25 min",
    imageUrl = ""
)

@Preview(showBackground = true, name = "1. Suggestions State")
@Composable
private fun PreviewContent_Suggestions() {
    CookingBuddyTheme {
        Content(
            query = "Pasta",
            isLoading = false,
            showFavorites = false,
            recipes = listOf(
                PreviewRecipe,
                PreviewRecipe.copy(id = 2, title = "Tomato Basil Soup"),
                PreviewRecipe.copy(id = 3, title = "Grilled Cheese")
            ),
            favorites = emptyList(),
            favoriteIds = setOf(1),
            onQueryChange = {},
            onSearch = {},
            onRecipeClick = {},
            onToggleFavorite = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, name = "2. Favorites State")
@Composable
private fun PreviewContent_Favorites() {
    CookingBuddyTheme {
        Content(
            query = "",
            isLoading = false,
            showFavorites = true,
            recipes = emptyList(),
            favorites = listOf(
                PreviewRecipe.copy(id = 4, title = "My Favorite Burger"),
                PreviewRecipe.copy(id = 5, title = "Mom's Lasagna")
            ),
            favoriteIds = setOf(4, 5),
            onQueryChange = {},
            onSearch = {},
            onRecipeClick = {},
            onToggleFavorite = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, name = "3. Empty State")
@Composable
private fun PreviewContent_Empty() {
    CookingBuddyTheme {
        Content(
            query = "Unicorn Meat",
            isLoading = false,
            showFavorites = false,
            recipes = emptyList(),
            favorites = emptyList(),
            favoriteIds = emptySet(),
            onQueryChange = {},
            onSearch = {},
            onRecipeClick = {},
            onToggleFavorite = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, name = "4. Loading State")
@Composable
private fun PreviewContent_Loading() {
    CookingBuddyTheme {
        Content(
            query = "Burger",
            isLoading = true,
            showFavorites = false,
            recipes = emptyList(),
            favorites = emptyList(),
            favoriteIds = emptySet(),
            onQueryChange = {},
            onSearch = {},
            onRecipeClick = {},
            onToggleFavorite = { _, _ -> }
        )
    }
}