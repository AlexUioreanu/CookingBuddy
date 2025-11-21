package com.example.cookingbuddy.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.cookingbuddy.ui.components.snackbar.SnackBarController
import com.example.cookingbuddy.ui.components.snackbar.SnackBarType
import com.example.cookingbuddy.ui.components.snackbar.SnackBarVisual
import com.example.cookingbuddy.ui.screens.RecipeSideEffect
import com.example.cookingbuddy.ui.screens.RecipeViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RecipeSideEffectHandler(viewModel: RecipeViewModel) {
    LaunchedEffect(viewModel.container.sideEffectFlow) {
        viewModel.container.sideEffectFlow.collectLatest { sideEffect ->
            when (sideEffect) {
                is RecipeSideEffect.Error -> {
                    SnackBarController.sendEvent(
                        SnackBarVisual(
                            snackBarType = SnackBarType.Error,
                            message = sideEffect.message,
                        )
                    )
                }
            }
        }
    }
}