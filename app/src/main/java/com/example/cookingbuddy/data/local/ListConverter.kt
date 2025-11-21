package com.example.cookingbuddy.data.local

import android.util.Log
import androidx.room.TypeConverter
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ListConverter {
    @TypeConverter
    fun fromString(value: String?): List<String> {
        if (value.isNullOrEmpty()) {
            return emptyList()
        }
        return try {
            Json.decodeFromString<List<String>>(value)
        } catch (e: SerializationException) {
            Log.e(
                "CookingBuddy",
                "Error parsing JSON. Falling back to lines. Raw Data: '$value'",
                e
            )
            value.lines()
        }
    }

    @TypeConverter
    fun toString(list: List<String>?): String {
        return Json.encodeToString(list ?: emptyList())
    }
}
