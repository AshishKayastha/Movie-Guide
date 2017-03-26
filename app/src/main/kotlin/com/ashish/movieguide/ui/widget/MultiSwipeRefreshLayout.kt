package com.ashish.movieguide.ui.widget

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.View
import com.ashish.movieguide.R
import java.util.ArrayList

/**
 * Created by Ashish on Dec 27.
 */
class MultiSwipeRefreshLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    var swipeableChildren = ArrayList<View>()

    init {
        val colors = resources.getIntArray(R.array.swipe_refresh_colors)
        setColorSchemeColors(*colors)
    }

    fun setSwipeableViews(vararg views: View) {
        for (view in views) {
            swipeableChildren.add(view)
        }
    }

    override fun canChildScrollUp(): Boolean {
        if (swipeableChildren.size > 0) {
            swipeableChildren
                    .filter { it.isShown && !ViewCompat.canScrollVertically(it, -1) }
                    .forEach { return false }
        }
        return true
    }

    override fun onDetachedFromWindow() {
        performCleanup()
        super.onDetachedFromWindow()
    }

    private fun performCleanup() {
        clearAnimation()
        setOnRefreshListener(null)
    }
}