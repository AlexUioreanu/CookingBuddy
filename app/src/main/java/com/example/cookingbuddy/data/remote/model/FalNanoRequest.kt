package com.example.cookingbuddy.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FalNanoRequest(
    val prompt: String,
    @SerialName("aspect_ratio") val aspectRatio: String = "1:1",
    @SerialName("sync_mode") val syncMode: Boolean = true,
    @SerialName("num_images") val numImages: Int = 1,
    @SerialName("output_format") val outputFormat: String = "jpeg"
)