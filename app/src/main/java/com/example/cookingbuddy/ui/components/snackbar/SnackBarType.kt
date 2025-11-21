package com.example.cookingbuddy.ui.components.snackbar

sealed class SnackBarType {
    data object Error : SnackBarType()
}