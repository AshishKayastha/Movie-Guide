package com.ashish.movieguide.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.extensions.convertToFullSpan
import com.ashish.movieguide.utils.extensions.inflate

/**
 * Created by Ashish on Dec 30.
 */
class LoadingDelegateAdapter : RecyclerViewDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return LoadingViewHolder(parent.inflate(R.layout.list_item_loading_footer))
                .also { it.convertToFullSpan() }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: RecyclerViewItem) {}

    private class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)
}