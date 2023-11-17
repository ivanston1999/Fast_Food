package com.android.fastfood

import android.app.Application
import com.android.fastfood.data.local.database.AppDatabase
import com.android.fastfood.dependencyinjection.AppModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@App)
            modules(AppModules.modules)
        }
    }}