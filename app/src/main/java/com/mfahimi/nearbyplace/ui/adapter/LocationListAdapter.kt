package com.mfahimi.nearbyplace.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mfahimi.nearbyplace.R
import com.mfahimi.nearbyplace.data.network.Response
import com.mfahimi.nearbyplace.ext.gone
import com.mfahimi.nearbyplace.ext.visible
import com.mfahimi.nearbyplace.model.LocationDataItem
import java.util.*


class LocationListAdapter(
    override var listener: AdapterListener<LocationDataItem>,
    override var dataList: ArrayList<AdapterDataModel<LocationDataItem>>
) : BaseAdapter<LocationDataItem>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (!isLoading && isMoreDataAvailable && position >= itemCount - 1) {
            isLoading = true
            listener.onLoadMore()
        } else if (holder is DataHolder && dataList[position] is AdapterDataModel.Data<LocationDataItem>) {
            holder.bind((dataList[position] as AdapterDataModel.Data<LocationDataItem>).data)
        } else if (holder is LoadingHolder && dataList[position] is AdapterDataModel.Loading<LocationDataItem>) {
            holder.bind((dataList[position] as AdapterDataModel.Loading<LocationDataItem>).data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_DATA)
            DataHolder.create(parent, listener::onItemClick)
        else
            LoadingHolder.create(parent, listener::retry)
    }
}

class DataHolder(view: View, private val detailCallback: ((LocationDataItem) -> Unit)?) :
    RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.title)
    val address: TextView = view.findViewById(R.id.address)

    fun bind(venue: LocationDataItem) {
        title.text = venue.name
        address.text = venue.address
        itemView.setOnClickListener {
            detailCallback?.invoke(venue)
        }
    }

    companion object {
        fun create(parent: ViewGroup, detailCallback: ((LocationDataItem) -> Unit)?): DataHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_location_list, parent, false)
            return DataHolder(view, detailCallback = detailCallback)
        }
    }
}

class LoadingHolder(view: View, private val retryCallback: (() -> Unit)?) :
    RecyclerView.ViewHolder(view) {
    private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
    private val retry = view.findViewById<Button>(R.id.retry_button)

    init {
        retry.setOnClickListener {
            retry.gone()
            progressBar.visible()
            retryCallback?.invoke()
        }
    }

    fun bind(networkState: Response<*>) {
        progressBar.visibility = toVisibility(networkState is Response.Loading)
        retry.visibility = toVisibility(networkState is Response.Error)
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: (() -> Unit)?): LoadingHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loading, parent, false)
            return LoadingHolder(view, retryCallback)
        }

        fun toVisibility(constraint: Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}
