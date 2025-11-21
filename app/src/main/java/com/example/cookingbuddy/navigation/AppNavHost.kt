package com.example.cookingbuddy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.cookingbuddy.ui.screens.RecipeViewModel
import com.example.cookingbuddy.ui.screens.details.RecipeDetailScreen
import com.example.cookingbuddy.ui.screens.home.HomeScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.RecipeGraph,
        modifier = modifier
    ) {
        recipeGraph(navController)
    }
}

fun NavGraphBuilder.recipeGraph(navController: NavHostController) {
    navigation<NavigationRoute.RecipeGraph>(
        startDestination = NavigationRoute.Home
    ) {
        composable<NavigationRoute.Home> { backStackEntry ->
            val graphEntry = remember(backStackEntry) {
                navController.getBackStackEntry(NavigationRoute.RecipeGraph)
            }
            val recipeViewModel: RecipeViewModel = koinViewModel(viewModelStoreOwner = graphEntry)
            HomeScreen(
                viewModel = recipeViewModel,
                onRecipeClick = {
                    navController.navigate(NavigationRoute.Detail)
                }
            )
        }

        composable<NavigationRoute.Detail> { backStackEntry ->
            val graphEntry = remember(backStackEntry) {
                navController.getBackStackEntry(NavigationRoute.RecipeGraph)
            }

            val recipeViewModel: RecipeViewModel = koinViewModel(viewModelStoreOwner = graphEntry)

            RecipeDetailScreen(
                viewModel = recipeViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}