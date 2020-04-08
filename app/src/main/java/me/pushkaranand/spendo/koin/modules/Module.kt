package me.pushkaranand.spendo.koin.modules

import me.pushkaranand.spendo.data.DataRepository
import me.pushkaranand.spendo.data.source.local.LocalDataSource
import me.pushkaranand.spendo.ui.activities.home.HomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val defaultModule = module {

    single {
        LocalDataSource(androidApplication())
    }

    single {
        DataRepository(get())
    }

}

val viewModelModules = module {

    viewModel { HomeViewModel(get()) }

}