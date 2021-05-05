package com.mfahimi.nearbyplace.di

import com.mfahimi.nearbyplace.viewModel.LocationDetailViewModel
import com.mfahimi.nearbyplace.viewModel.LocationListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LocationListViewModel(get()) }
    viewModel { LocationDetailViewModel(get()) }
}