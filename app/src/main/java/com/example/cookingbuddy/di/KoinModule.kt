package com.example.cookingbuddy.di

import androidx.room.Room
import com.example.cookingbuddy.BuildConfig
import com.example.cookingbuddy.data.local.RecipeDatabase
import com.example.cookingbuddy.data.remote.AiRecipeGenerator
import com.example.cookingbuddy.data.remote.FalImageDataSource
import com.example.cookingbuddy.data.remote.ImageDataSource
import com.example.cookingbuddy.data.repository.RecipeRepository
import com.example.cookingbuddy.data.repository.RecipeRepositoryImpl
import com.example.cookingbuddy.domain.usecase.AddFavoriteUseCase
import com.example.cookingbuddy.domain.usecase.GetFavoritesUseCase
import com.example.cookingbuddy.domain.usecase.GetRecipesUseCase
import com.example.cookingbuddy.domain.usecase.RemoveFavoriteUseCase
import com.example.cookingbuddy.ui.screens.RecipeViewModel
import com.example.cookingbuddy.ui.utils.ResourcesProvider
import com.example.cookingbuddy.ui.utils.ResourcesProviderImpl
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

private val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
            encodeDefaults = true
        }
    }

    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        GenerativeModel(
            modelName = "gemini-flash-latest",
            apiKey = BuildConfig.GEMINI_API_KEY,
            generationConfig = generationConfig {
                temperature = 0.4f
                responseMimeType = "application/json"
            }
        )
    }

    singleOf(::FalImageDataSource) { bind<ImageDataSource>() }
    singleOf(::AiRecipeGenerator)
}

private val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), RecipeDatabase::class.java, "recipe_database")
            .fallbackToDestructiveMigration(true)
            .build()
    }
    single { get<RecipeDatabase>().recipeDao() }
}

private val domainModule = module {
    single<RecipeRepository> { RecipeRepositoryImpl(get(), get()) }

    singleOf(::GetRecipesUseCase)
    singleOf(::GetFavoritesUseCase)
    singleOf(::AddFavoriteUseCase)
    singleOf(::RemoveFavoriteUseCase)
}

private val uiModule = module {
    single<ResourcesProvider> { ResourcesProviderImpl(androidContext()) }
    viewModelOf(::RecipeViewModel)
}

val appModule = module {
    includes(networkModule, databaseModule, domainModule, uiModule)
}