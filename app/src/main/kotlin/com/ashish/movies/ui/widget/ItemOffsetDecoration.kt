package com.ashish.movies.ui.widget

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ashish.movies.utils.extensions.dpToPx

/**
 * Created by Ashish on Dec 29.
 */
class ItemOffsetDecoration(val spacing: Int = 4f.dpToPx().toInt()) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        outRect.set(spacing, spacing, spacing, spacing)
    }
}