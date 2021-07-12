package com.example.mohammeawadtask.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mohammeawadtask.R
import com.example.mohammeawadtask.data.Data
import com.example.mohammeawadtask.repository.NetworkStatus

class CarPageListAdapter(val context: Context) :
    PagedListAdapter<Data, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkStatus: NetworkStatus? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as CarItemViewHolder).bind(getItem(position), context)
        } else
            (holder as NetworkStatItemViewHolder).bind(networkStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        return if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.car_list_item, parent, false)
            CarItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            NetworkStatItemViewHolder(view)
        }
    }


    class MovieDiffCallback : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == oldItem
        }

    }

    private fun hasExtraRow(): Boolean {
        return networkStatus != null && networkStatus != NetworkStatus.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {

        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else
            MOVIE_VIEW_TYPE
    }

    class CarItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: Data?, context: Context) {
            itemView.findViewById<TextView>(R.id.cv_car_title).text = data?.brand
            if (data?.isUsed!!) {
                itemView.findViewById<TextView>(R.id.cv_car_is_used).text = "Used"
            } else {
                itemView.findViewById<TextView>(R.id.cv_car_is_used).text = "New"
            }
            itemView.findViewById<TextView>(R.id.cv_car_release_date).text =
                data?.constractionYear
            val moviePoster = data?.imageUrl

            Glide.with(itemView.context)
                .load(moviePoster)
                .placeholder(getDrawable(context, R.drawable.placeholder))
                .into(itemView.findViewById(R.id.cv_iv_car_poster))


        }


    }

    fun setNetworkState(newNetworkState: NetworkStatus) {
        val previousState: NetworkStatus? = this.networkStatus
        val hadExtraRow = hasExtraRow()
        this.networkStatus = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }

        } else if (hasExtraRow && previousState != networkStatus) {
            notifyItemChanged(itemCount - 1)
        }


    }

    class NetworkStatItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkStatus?) {
            val errorTextView: TextView = itemView.findViewById(R.id.error_msg_item)
            val progressBar = itemView.findViewById<ProgressBar>(R.id.progress_bar_item)

            if (networkState != null && networkState == NetworkStatus.LOADING) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = GONE
            }

            if (networkState != null && networkState == NetworkStatus.ERROR) {
                errorTextView.visibility = View.VISIBLE
                errorTextView.text = networkState.msg
            } else if (networkState != null && networkState == NetworkStatus.END_OF_LIST) {
                errorTextView.visibility = View.VISIBLE
                errorTextView.text = networkState.msg
            } else {
                errorTextView.visibility = GONE
            }
        }
    }


}