package com.example.cookingbuddy.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FalImageResult(
    val url: String,
    @SerialName("content_type") val contentType: String? = null
)