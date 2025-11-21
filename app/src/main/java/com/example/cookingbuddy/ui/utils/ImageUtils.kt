package com.example.cookingbuddy.ui.utils

import android.util.Base64
import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import coil.request.CachePolicy
import coil.request.ImageRequest

@Composable
fun rememberRecipeImageRequest(imageUrl: String): ImageRequest {
    val context = LocalContext.current

    return remember(imageUrl) {
        val imageModel: Any = if (imageUrl.startsWith("data:image")) {
            try {
                val base64Data = imageUrl.substringAfter(",")
                Base64.decode(base64Data, Base64.DEFAULT)
            } catch (e: Exception) {
                Log.e("CookingBuddy", "Failed to decode Base64", e)
                imageUrl
            }
        } else {
            imageUrl
        }

        ImageRequest.Builder(context)
            .data(imageModel)
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .listener(
                onStart = { Log.d("CookingBuddy", "Start loading: $imageUrl") },
                onError = { _, res -> Log.e("CookingBuddy", "Error: ${res.throwable.message}") },
                onSuccess = { _, _ -> Log.d("CookingBuddy", "Success") }
            )
            .build()
    }
}

@Composable
fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_float"
    )

    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )

    this.background(brush)
}