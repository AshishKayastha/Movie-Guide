package com.ashish.movieguide.ui.base.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.utils.extensions.inflate

abstract class BaseHolder(
        parent: ViewGroup,
        layoutId: Int
) : RecyclerView.ViewHolder(parent.inflate(layoutId)) {

    open fun attachListener(onItemClickListener: OnItemClickListener?) {
        itemView.setOnClickListener { view ->
            onItemClickListener?.onItemClick(adapterPosition, view)
        }
    }
}