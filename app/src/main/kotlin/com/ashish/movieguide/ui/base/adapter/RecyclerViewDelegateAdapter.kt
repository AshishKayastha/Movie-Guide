package com.ashish.movieguide.ui.base.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

interface RecyclerViewDelegateAdapter {

    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: RecyclerViewItem)
}