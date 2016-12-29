package com.ashish.movies.ui.widget

import android.content.res.Resources
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by Ashish on Dec 29.
 */
class ItemOffsetDecoration : RecyclerView.ItemDecoration() {

    companion object {
        val ITEM_SPACING = (4 * Resources.getSystem().displayMetrics.density).toInt()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        outRect.set(ITEM_SPACING, ITEM_SPACING, ITEM_SPACING, ITEM_SPACING)
    }
}