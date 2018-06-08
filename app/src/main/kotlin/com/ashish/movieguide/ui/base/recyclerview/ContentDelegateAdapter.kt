package com.ashish.movieguide.ui.base.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RemoveListener
import com.ashish.movieguide.ui.common.adapter.ViewTypeDelegateAdapter

abstract class ContentDelegateAdapter(
        private val layoutId: Int,
        private var onItemClickListener: OnItemClickListener?
) : ViewTypeDelegateAdapter, RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return getHolder(parent, layoutId).also { it.attachListener(onItemClickListener) }
    }

    abstract fun getHolder(parent: ViewGroup, layoutId: Int): BaseHolder

    override fun removeListener() {
        onItemClickListener = null
    }
}