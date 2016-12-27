package com.ashish.movies.extensions

import android.view.View

/**
 * Created by Ashish on Dec 27.
 */
fun View.setVisibility(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}