package com.ashish.movies.ui.widget

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ashish.movies.utils.extensions.dpToPx

/**
 * Created by Ashish on Dec 29.
 */
class ItemOffsetDecoration : RecyclerView.ItemDecoration() {

    companion object {
        @JvmStatic val ITEM_SPACING = 4f.dpToPx().toInt()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        outRect.set(ITEM_SPACING, ITEM_SPACING, ITEM_SPACING, ITEM_SPACING)
    }
}