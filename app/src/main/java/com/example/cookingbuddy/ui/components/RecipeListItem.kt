package com.example.cookingbuddy.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.cookingbuddy.R
import com.example.cookingbuddy.domain.model.Recipe
import com.example.cookingbuddy.ui.theme.CookingBuddyTheme
import com.example.cookingbuddy.ui.theme.bodyTextStyle
import com.example.cookingbuddy.ui.theme.lightEmphasize
import com.example.cookingbuddy.ui.theme.smallXTextStyle
import com.example.cookingbuddy.ui.utils.rememberRecipeImageRequest
import com.example.cookingbuddy.ui.utils.shimmerEffect

@Composable
fun RecipeListItem(
    recipe: Recipe,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    val imageRequest = rememberRecipeImageRequest(recipe.imageUrl)

    Card(
        modifier = Modifier
            .height(88.dp)
            .fillMaxWidth()
            .dropShadow(
                shape = RoundedCornerShape(16.dp),
                shadow = Shadow(
                    radius = 8.dp,
                    spread = 0.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    offset = DpOffset(x = 4.dp, 4.dp),
                    blendMode = BlendMode.SrcOver
                )
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = imageRequest,
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.25F),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmerEffect()
                    )
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.errorContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.place_holder_image),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = recipe.title,
                    style = bodyTextStyle().lightEmphasize(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(recipe.duration, style = smallXTextStyle())
            }

            FavoriteIcon(onFavoriteClick = onFavoriteClick, isFavorite = isFavorite)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipeListItemPreview() {
    val recipe = Recipe(
        id = 1,
        title = "Delicious Pasta",
        ingredients = listOf("Pasta", "Tomato Sauce", "Cheese"),
        instructions = listOf(
            "Boil water in a large pot.",
            "Add pasta and cook for 10 minutes.",
            "Drain water and mix with sauce."
        ),
        imageUrl = "",
        duration = "20 min"
    )
    CookingBuddyTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RecipeListItem(
                recipe = recipe,
                isFavorite = true,
                onFavoriteClick = {},
                onClick = {}
            )
            RecipeListItem(
                recipe = recipe,
                isFavorite = true,
                onFavoriteClick = {},
                onClick = {}
            )
            RecipeListItem(
                recipe = recipe,
                isFavorite = true,
                onFavoriteClick = {},
                onClick = {}
            )
        }
    }
}