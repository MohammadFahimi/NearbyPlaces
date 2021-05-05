package com.mfahimi.nearbyplace.ui.adapter

interface AdapterListener<T> {
    fun onLoadMore()
    fun retry()
    fun onItemClick(item:T)
}