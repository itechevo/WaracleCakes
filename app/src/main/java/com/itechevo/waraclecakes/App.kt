package com.itechevo.waraclecakes

import android.app.Application
import com.itechevo.waraclecakes.di.appModule
import com.itechevo.waraclecakes.di.dataModule
import com.itechevo.waraclecakes.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                listOf(
                    domainModule,
                    dataModule,
                    appModule
                )
            )
        }
    }
}