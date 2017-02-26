package com.ashish.movieguide.ui.widget

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.util.AttributeSet
import com.ashish.movieguide.utils.FontUtils

/**
 * Created by Ashish on Jan 07.
 */
class FontCheckBox : AppCompatCheckBox {

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        FontUtils.applyFont(this, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        FontUtils.applyFont(this, attrs)
    }
}