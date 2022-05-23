package com.coding.flickertask.di.module

import com.coding.flickertask.api.GetPhotoService
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single {
        val retrofit: Retrofit = get()
        retrofit.create(GetPhotoService::class.java)
    }
}
