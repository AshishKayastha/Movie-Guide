package com.ashish.movies.ui.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by Ashish on Jan 15.
 */
class IgnoreBottomInsetFrameLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                             defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    @Suppress("DEPRECATION", "OverridingDeprecatedMember")
    override fun fitSystemWindows(insets: Rect): Boolean {
        insets.bottom = 0
        return super.fitSystemWindows(insets)
    }
}