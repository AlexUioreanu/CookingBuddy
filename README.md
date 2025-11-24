<div align="center">
  <h1>Cooking Buddy</h1>
  
  <img width="200" alt="Cooking Buddy Logo" src="https://github.com/user-attachments/assets/cc29593b-ee3d-4404-9710-76c7f5df81d2" />

  <p>
    An intelligent recipe generator and cookbook app built with the latest Android technologies. <br>
    Discover, generate, and save your favorite recipes, all powered by AI.
  </p>
</div>

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
