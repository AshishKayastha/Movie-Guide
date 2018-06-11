package com.ashish.movieguide.ui.base.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.utils.extensions.inflate

abstract class ContentDelegateAdapter(
        private val layoutId: Int,
        private var onItemClickListener: OnItemClickListener?
) : RecyclerViewDelegateAdapter, RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return getHolder(parent.inflate(layoutId))
                .also { it.attachListener(onItemClickListener) }
    }

    abstract fun getHolder(view: View): BaseHolder

    override fun removeListener() {
        onItemClickListener = null
    }
}