package com.coding.flickertask.di.module

import com.coding.flickertask.di.repository.LocalStorageRepository
import org.koin.dsl.module

val dataStoreModule = module {
    single { LocalStorageRepository(get()) }
}