package com.ashish.movieguide.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.ViewGroup
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.extensions.inflate
import kotlinx.android.synthetic.main.list_item_error_load_more.view.*

class LoadMoreErrorDelegateAdapter(
        private var onItemClickListener: OnItemClickListener?
) : ViewTypeDelegateAdapter, RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val holder = LoadMoreErrorViewHolder(parent)
        val params = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        params.isFullSpan = true
        holder.itemView.layoutParams = params
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {}

    override fun removeListener() {
        onItemClickListener = null
    }

    inner class LoadMoreErrorViewHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_error_load_more)) {

        init {
            itemView.retryBtn.setOnClickListener {
                onItemClickListener?.onItemClick(adapterPosition, it)
            }
        }
    }
}