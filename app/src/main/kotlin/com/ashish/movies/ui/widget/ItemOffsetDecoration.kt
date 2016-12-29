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
        if (parent.paddingLeft != ITEM_SPACING) {
            parent.setPadding(ITEM_SPACING, parent.paddingTop, ITEM_SPACING, ITEM_SPACING)
            parent.clipToPadding = false
        }

        outRect.left = ITEM_SPACING
        outRect.right = ITEM_SPACING
        outRect.bottom = ITEM_SPACING
    }
}