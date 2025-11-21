package com.example.cookingbuddy.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cookingbuddy.R
import com.example.cookingbuddy.ui.theme.CookingBuddyTheme

@Composable
fun FavoriteIcon(
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { onFavoriteClick() }
    ) {
        Icon(
            painter =
                if (isFavorite)
                    painterResource(R.drawable.ic_favorite_fill)
                else
                    painterResource(R.drawable.ic_favorite),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteIconPreview() {
    CookingBuddyTheme {
        Column(Modifier.padding(16.dp)) {
            FavoriteIcon(onFavoriteClick = {}, isFavorite = true)
            FavoriteIcon(onFavoriteClick = {}, isFavorite = false)
        }
    }
}