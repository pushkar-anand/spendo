package me.pushkaranand.spendo

import android.app.Application
import me.pushkaranand.spendo.koin.modules.defaultModule
import me.pushkaranand.spendo.koin.modules.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Spendo : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Spendo)
            modules(
                defaultModule,
                viewModelModules
            )
        }
    }
}