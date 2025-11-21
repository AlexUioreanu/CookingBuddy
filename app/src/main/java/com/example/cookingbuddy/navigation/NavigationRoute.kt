package com.example.cookingbuddy.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoute {

    @Serializable
    data object RecipeGraph : NavigationRoute()

    @Serializable
    data object Home : NavigationRoute()

    @Serializable
    data object Detail : NavigationRoute()
}