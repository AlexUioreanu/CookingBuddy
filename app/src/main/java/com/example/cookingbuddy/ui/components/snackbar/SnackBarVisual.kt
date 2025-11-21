package com.example.cookingbuddy.ui.components.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

data class SnackBarVisual(
    override val message: String,
    val title: String? = null,
    val snackBarType: SnackBarType,
    override val withDismissAction: Boolean = true,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val actionLabel: String? = null,
) : SnackbarVisuals