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
        startDestination = RecipeGraph,
        modifier = modifier
    ) {
        recipeGraph(navController)
    }
}

fun NavGraphBuilder.recipeGraph(navController: NavHostController) {
    navigation<RecipeGraph>(
        startDestination = NavigationRoute.Home
    ) {
        composable<NavigationRoute.Home> { backStackEntry ->
            val viewModel = sharedViewModel<RecipeViewModel>(navController, backStackEntry)

            HomeScreen(
                viewModel = viewModel,
                onRecipeClick = {
                    navController.navigate(NavigationRoute.Detail)
                }
            )
        }

        composable<NavigationRoute.Detail> { backStackEntry ->
            val viewModel = sharedViewModel<RecipeViewModel>(navController, backStackEntry)

            RecipeDetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private inline fun <reified T : androidx.lifecycle.ViewModel> sharedViewModel(
    navController: NavHostController,
    currentEntry: androidx.navigation.NavBackStackEntry
): T {
    val parentEntry = remember(currentEntry) {
        navController.getBackStackEntry(RecipeGraph)
    }
    return koinViewModel(viewModelStoreOwner = parentEntry)
}