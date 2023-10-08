package com.android.fastfood

import android.app.Application
import com.android.fastfood.data.local.database.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)
    }
}