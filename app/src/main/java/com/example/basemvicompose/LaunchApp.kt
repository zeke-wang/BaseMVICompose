package com.example.basemvicompose

import android.app.Application
import com.example.basemvicompose.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class LaunchApp :Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@LaunchApp)
            modules(appModules)
        }
    }
}