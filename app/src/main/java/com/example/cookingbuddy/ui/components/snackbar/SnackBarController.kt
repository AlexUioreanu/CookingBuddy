package com.example.cookingbuddy.ui.components.snackbar

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object SnackBarController {

    private val _events = Channel<SnackBarVisual>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackBarVisual) {
        _events.send(event)
    }
}