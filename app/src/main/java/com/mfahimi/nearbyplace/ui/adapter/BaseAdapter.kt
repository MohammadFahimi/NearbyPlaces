package com.mfahimi.nearbyplace.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.mfahimi.nearbyplace.data.network.Response
import java.util.*

abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    abstract var dataList: ArrayList<AdapterDataModel<T>>
    abstract var listener: AdapterListener<T>
    protected val TYPE_DATA = 1
    protected var isLoading = false
    protected var isMoreDataAvailable: Boolean = false

    override fun getItemViewType(position: Int): Int {
        return if (dataList[position] is AdapterDataModel.Data<*>)
            TYPE_DATA
        else
            0
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setIsMoreDataAvailable(isMoreDataAvailable: Boolean) {
        this.isMoreDataAvailable = isMoreDataAvailable
    }

    fun submitList(items: List<T>) {
        removeLoadingItem()
        dataList.addAll(items.map { AdapterDataModel.Data(it) })
        notifyItemInserted(dataList.size - 1)
    }

    fun clearList() {
        dataList.clear()
        notifyDataSetChanged()
    }

    fun addLoadingItem() {
        isLoading = true
        dataList.add(AdapterDataModel.Loading(Response.Loading(null)))
    }

    fun updateLoadingState(newState: Response<Nothing>) {
        dataList.set(dataList.size - 1, AdapterDataModel.Loading(newState))
    }

    open fun removeLoadingItem() {
        if (dataList.size > 0 && dataList[dataList.size - 1] is AdapterDataModel.Loading) {
            dataList.removeAt(dataList.size - 1)
            notifyItemRemoved(dataList.size - 1)
        }
        isLoading = false
    }
}