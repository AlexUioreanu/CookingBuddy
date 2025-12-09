package com.example.cookingbuddy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cookingbuddy.navigation.AppNavHost
import com.example.cookingbuddy.ui.components.snackbar.CustomSnackBar
import com.example.cookingbuddy.ui.components.snackbar.SnackBarController
import com.example.cookingbuddy.ui.components.snackbar.SnackBarVisual
import com.example.cookingbuddy.ui.theme.CookingBuddyTheme
import com.example.cookingbuddy.ui.utils.ObserveAsEvents
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinExperimentalAPI

class MainActivity : ComponentActivity() {
    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CookingBuddyTheme {
                MainScreen()
            }
        }
        
        // Keys are now available via BuildConfig
        val geminiApiKey = BuildConfig.GEMINI_API_KEY
        Log.d("MainActivity", "Gemini API Key: $geminiApiKey")
        
        val falApiKey = BuildConfig.FAL_API_KEY
        val falApiBaseUrl = BuildConfig.FAL_API_BASE_URL
        val loremFlickrUrl = BuildConfig.LOREM_FLICKR_URL
    }
}

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    ObserveAsEvents(flow = SnackBarController.events, key1 = snackBarHostState) { event ->
        scope.launch {
            snackBarHostState.currentSnackbarData?.dismiss()
            snackBarHostState.showSnackbar(visuals = event)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                val visual = data.visuals as SnackBarVisual
                CustomSnackBar(
                    snackBarType = visual.snackBarType,
                    title = visual.title,
                    message = visual.message,
                    dismiss = { data.dismiss() },
                )
            }
        },
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
