package com.example.cookingbuddy.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cookingbuddy.ui.details.RecipeDetailScreen
import com.example.cookingbuddy.ui.home.HomeScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier
    ) {
        composable<Screen.Home> {
            HomeScreen(
                homeViewModel = koinViewModel(),
                onRecipeClick = { recipe ->
                    // Navigate with only the recipe ID
                    navController.navigate(Screen.Detail(recipe.id))
                }
            )
        }
        composable<Screen.Detail> {
            RecipeDetailScreen(
                viewModel = koinViewModel(),
                navController = navController
            )
        }
    }
}