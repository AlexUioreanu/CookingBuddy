package com.example.cookingbuddy.ui.screens.details

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.example.cookingbuddy.R
import com.example.cookingbuddy.domain.model.Recipe
import com.example.cookingbuddy.ui.components.FavoriteIcon
import com.example.cookingbuddy.ui.screens.RecipeViewModel
import com.example.cookingbuddy.ui.theme.CookingBuddyTheme
import com.example.cookingbuddy.ui.theme.bodyTextStyle
import com.example.cookingbuddy.ui.theme.headlineTextStyle
import com.example.cookingbuddy.ui.theme.smallTextStyle
import com.example.cookingbuddy.ui.utils.RecipeSideEffectHandler
import com.example.cookingbuddy.ui.utils.rememberRecipeImageRequest
import com.example.cookingbuddy.ui.utils.shimmerEffect

@Composable
fun RecipeDetailScreen(
    viewModel: RecipeViewModel,
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val recipe = uiState.selectedRecipe ?: return

    val isFavorite by remember(uiState.favorites, recipe.id) {
        derivedStateOf {
            uiState.favorites.any { it.id == recipe.id }
        }
    }

    RecipeSideEffectHandler(viewModel)

    Content(
        recipe = recipe,
        isFavorite = isFavorite,
        onBack = onBack,
        onToggleFavorite = {
            viewModel.onFavoriteClicked(recipe, isFavorite)
        }
    )
}

@Composable
private fun Content(
    recipe: Recipe,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        item {
            HeaderImage(
                imageUrl = recipe.imageUrl,
                contentDescription = recipe.title,
                onBack = onBack
            )
        }

        item {
            TitleSection(
                title = recipe.title,
                duration = recipe.duration,
                isFavorite = isFavorite,
                onToggleFavorite = onToggleFavorite
            )
        }

        item {
            SectionHeader(titleRes = R.string.title_ingredients)
        }
        items(recipe.ingredients) { ingredient ->
            IngredientRow(text = ingredient)
        }

        item {
            SectionHeader(titleRes = R.string.title_instructions)
        }
        itemsIndexed(recipe.instructions) { index, instruction ->
            InstructionRow(
                index = index + 1,
                text = instruction
            )
        }

        item { Spacer(Modifier.height(16.dp)) }
    }
}

@Composable
private fun HeaderImage(
    imageUrl: String,
    contentDescription: String,
    onBack: () -> Unit
) {
    val imageRequest = rememberRecipeImageRequest(imageUrl)

    Box(modifier = Modifier.fillMaxWidth()) {
        SubcomposeAsyncImage(
            model = imageRequest,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .height(360.dp),
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

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(start = 2.dp, top = 8.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun TitleSection(
    title: String,
    duration: String,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 4.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = headlineTextStyle()
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = duration,
                style = smallTextStyle()
            )
        }

        FavoriteIcon(
            onFavoriteClick = onToggleFavorite,
            isFavorite = isFavorite
        )
    }
}

@Composable
private fun SectionHeader(
    @StringRes titleRes: Int
) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(titleRes),
        style = bodyTextStyle().copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
    )
}

@Composable
private fun IngredientRow(text: String) {
    Row(
        modifier = Modifier.padding(start = 24.dp, end = 16.dp, bottom = 2.dp),
    ) {
        Text(
            text = stringResource(R.string.dot),
            modifier = Modifier.padding(end = 8.dp),
            style = bodyTextStyle()
        )
        Text(
            text = text,
            style = smallTextStyle()
        )
    }
}

@Composable
private fun InstructionRow(index: Int, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 16.dp, bottom = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = stringResource(R.string.step_number_format, index),
            style = smallTextStyle(),
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text.trim(),
            style = smallTextStyle(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentPreview() {
    CookingBuddyTheme {
        Content(
            recipe = Recipe(
                id = 1,
                title = "Classic Spaghetti Carbonara",
                duration = "30 min",
                ingredients = listOf(
                    "200g spaghetti",
                    "100g pancetta or guanciale, diced",
                    "2 large eggs",
                    "50g Pecorino Romano cheese, freshly grated",
                    "50g Parmesan cheese, freshly grated,50g Parmesan cheese, freshly grated",
                    "2 cloves garlic, minced",
                    "Freshly ground black pepper",
                    "Salt to taste",
                    "A handful of fresh parsley, chopped"
                ),
                instructions = listOf(
                    "Cook the spaghetti in a large pot of salted boiling water according to package directions until al dente.",
                    "While the pasta is cooking, heat a large skillet over medium heat. Add the pancetta and cook until crisp and golden, about 5-7 minutes. Add the minced garlic and cook for another minute until fragrant. Remove from heat.",
                    "In a medium bowl, whisk together the eggs and grated cheeses. Season generously with black pepper.",
                    "Once the pasta is cooked, reserve about 1/2 cup of the pasta water, then drain the spaghetti.",
                    "Immediately add the hot spaghetti to the skillet with the pancetta. Toss to combine. Pour the egg and cheese mixture over the pasta, stirring quickly to create a creamy sauce. If the sauce is too thick, add a little of the reserved pasta water until it reaches your desired consistency.",
                    "Season with salt if needed. Serve immediately, garnished with extra grated cheese and fresh parsley."
                ),
                imageUrl = ""
            ),
            isFavorite = true,
            onBack = {},
            onToggleFavorite = {}
        )
    }
}