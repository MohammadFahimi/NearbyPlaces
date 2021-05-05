package com.mfahimi.nearbyplace.ui.adapter

import com.mfahimi.nearbyplace.data.network.Response

sealed class AdapterDataModel<T> {
    class Data<T>(val data: T) : AdapterDataModel<T>()
    class Loading<T>(val data: Response<Nothing>) : AdapterDataModel<T>()
}
