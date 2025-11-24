<p align="center">
  <img src="https://storage.googleapis.com/project-logos/c6275858-c01b-41d3-a42e-223407635d3d.png" alt="Cooking Buddy Logo" width="200">
</p>

<h1 align="center">Cooking Buddy</h1>

<p align="center">
  An intelligent recipe generator and cookbook app built with the latest Android technologies. Discover, generate, and save your favorite recipes, all powered by AI.
</p>

## ✨ Features

*   **AI-Powered Recipe Generation**: Leverages the Google Gemini API to generate unique and relevant recipes based on your search queries (e.g., "chicken and rice dishes", "healthy breakfast ideas").
*   **AI-Generated Recipe Images**: Utilizes the Fal AI API to create beautiful, unique images for each generated recipe, providing a rich visual experience.
*   **Favorite Recipes**: Save your favorite recipes to your personal digital cookbook for quick and easy access.
*   **Detailed Recipe View**: Get a complete breakdown of each recipe, including ingredients and step-by-step instructions.
*   **Clean, Modern UI**: Built entirely with Jetpack Compose and Material 3, providing a smooth, intuitive, and modern user interface.
*   **Robust Local Caching**: Saves your favorite recipes locally using a Room database.

## 📸 Screenshots

<!-- Add your screenshots here! Create a 'screenshots' folder in your project root. -->
| Home Screen                                     | Recipe Detail                                       |
| ----------------------------------------------- | --------------------------------------------------- |
| <img src="./screenshots/home.png" width="250">  | <img src="./screenshots/detail.png" width="250">    |

## 🛠 Tech Stack & Architecture

This project is built with a modern Android architecture and showcases the use of various popular libraries.

*   **UI**: Jetpack Compose, Material 3
*   **Architecture**: MVI (Model-View-Intent) using [Orbit MVI](https://orbit-mvi.org/)
*   **Dependency Injection**: [Koin](https://insert-koin.io/)
*   **Asynchronous Programming**: Kotlin Coroutines & Flow
*   **Networking**: OkHttp
*   **Local Storage**: Room Database
*   **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
*   **AI APIs**:
    *   Google Gemini for recipe generation
    *   Fal AI for image generation
*   **Serialization**: Kotlinx Serialization, Gson (for TypeConverters)
