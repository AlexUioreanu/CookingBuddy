package com.example.cookingbuddy.ui.utils

import android.content.Context
import androidx.annotation.StringRes

interface ResourcesProvider {
    fun getString(@StringRes stringResId: Int): String
    fun <T : Any> getString(@StringRes stringResId: Int, vararg formatArgs: T): String
}

class ResourcesProviderImpl(
    private val context: Context,
) : ResourcesProvider {

    override fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }

    override fun <T : Any> getString(stringResId: Int, vararg formatArgs: T): String {
        return context.getString(stringResId, *formatArgs)
    }
}
