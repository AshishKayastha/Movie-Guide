package com.ashish.movies.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by Ashish on Dec 30.
 */
interface ViewTypeDelegateAdapter {

    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType)
}