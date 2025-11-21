package com.example.cookingbuddy.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class FalResponse(
    val images: List<FalImageResult>
)