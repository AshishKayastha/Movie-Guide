package com.ashish.movieguide.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.base.adapter.BaseHolder
import com.ashish.movieguide.ui.base.adapter.ContentDelegateAdapter
import com.ashish.movieguide.ui.base.adapter.RecyclerViewItem
import com.ashish.movieguide.ui.widget.FontButton
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.convertToFullSpan

class LoadMoreErrorDelegateAdapter(
        onItemClickListener: OnItemClickListener?
) : ContentDelegateAdapter(R.layout.list_item_error_load_more, onItemClickListener) {

    override fun getHolder(view: View): BaseHolder {
        return LoadMoreErrorViewHolder(view).also { it.convertToFullSpan() }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: RecyclerViewItem) {}

    private class LoadMoreErrorViewHolder(view: View) : BaseHolder(view) {

        private val retryBtn: FontButton by bindView(R.id.retry_btn)

        override fun attachListener(onItemClickListener: OnItemClickListener?) {
            retryBtn.setOnClickListener {
                onItemClickListener?.onItemClick(adapterPosition, it)
            }
        }
    }
}