package com.ashish.movieguide.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.ViewGroup
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.base.recyclerview.BaseHolder
import com.ashish.movieguide.ui.widget.FontButton
import com.ashish.movieguide.utils.extensions.bindView

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

    private class LoadMoreErrorViewHolder(
            parent: ViewGroup
    ) : BaseHolder(parent, R.layout.list_item_error_load_more) {

        private val retryBtn: FontButton by bindView(R.id.retry_btn)

        override fun attachListener(onItemClickListener: OnItemClickListener?) {
            retryBtn.setOnClickListener {
                onItemClickListener?.onItemClick(adapterPosition, it)
            }
        }
    }
}