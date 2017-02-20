package com.ashish.movies.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.ViewGroup
import com.ashish.movies.R
import com.ashish.movies.utils.extensions.inflate

/**
 * Created by Ashish on Dec 30.
 */
class LoadingDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val holder = LoadingViewHolder(parent)
        val params = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        params.isFullSpan = true
        holder.itemView.layoutParams = params
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {}

    inner class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.list_item_loading_footer))
}