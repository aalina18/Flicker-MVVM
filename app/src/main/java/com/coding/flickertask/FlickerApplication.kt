package com.coding.flickertask

import android.app.Application
import com.coding.flickertask.di.module.MainViewModelModule
import com.coding.flickertask.networking.networkModule
import com.coding.flickertask.di.module.apiModule
import com.coding.flickertask.di.module.dataStoreModule
import com.coding.flickertask.di.module.repositoryModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@FlowPreview
@ExperimentalCoroutinesApi
class FlickerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@FlickerApplication)
            modules(listOf(
                MainViewModelModule.modules,
                repositoryModule,
                dataStoreModule,
                apiModule,
                networkModule
            ))
        }
    }
}

