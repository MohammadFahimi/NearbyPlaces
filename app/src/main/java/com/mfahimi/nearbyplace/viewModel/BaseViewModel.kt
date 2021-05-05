package com.mfahimi.nearbyplace.viewModel

import androidx.lifecycle.ViewModel
import com.mfahimi.nearbyplace.data.network.Response
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseViewModel : ViewModel() {
    fun <T> mutableStateFlow() = MutableStateFlow<Response<T>>(Response.Idle)
    fun <T> mutableSharedFlow() = MutableSharedFlow<Response<T>>()
}

fun <T> MutableStateFlow<Response<T>>.immutable() = this.asStateFlow()
fun <T> MutableSharedFlow<Response<T>>.immutable() = this.asSharedFlow()