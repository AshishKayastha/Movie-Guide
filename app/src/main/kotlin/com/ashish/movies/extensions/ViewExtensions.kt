package com.ashish.movies.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

/**
 * Created by Ashish on Dec 27.
 */
fun View.setVisibility(visible: Boolean) {
    visibility = if (visible) VISIBLE else GONE
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View? {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}