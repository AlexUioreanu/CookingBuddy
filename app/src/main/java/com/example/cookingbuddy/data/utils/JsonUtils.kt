package com.example.cookingbuddy.data.utils

fun extractJsonArray(text: String): String {
    val pattern = "\\[.*]".toRegex(RegexOption.DOT_MATCHES_ALL)
    return pattern.find(text)?.value ?: "[]"
}