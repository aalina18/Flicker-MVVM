package com.coding.flickertask.di.module

import com.coding.flickertask.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class MainViewModelModule {
	companion object{
		@ExperimentalCoroutinesApi
		val modules = module {
			viewModel { MainViewModel(get(),get()) }
		}
	}
}
