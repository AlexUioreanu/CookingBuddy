package com.example.cookingbuddy

import android.app.Application
import com.example.cookingbuddy.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class CookingBuddyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CookingBuddyApplication)

            modules(
                appModule
            )
        }
    }
}