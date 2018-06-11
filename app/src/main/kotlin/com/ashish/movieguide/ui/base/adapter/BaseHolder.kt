package com.ashish.movieguide.ui.base.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener

abstract class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {

    open fun attachListener(onItemClickListener: OnItemClickListener?) {
        itemView.setOnClickListener { view ->
            onItemClickListener?.onItemClick(adapterPosition, view)
        }
    }
}