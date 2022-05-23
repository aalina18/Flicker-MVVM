package com.coding.flickertask.di.module

import com.coding.flickertask.data.PhotoRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { PhotoRepository(get()) }
}