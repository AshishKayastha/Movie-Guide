package com.ashish.movieguide.utils.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.support.annotation.ColorInt
import android.text.style.TypefaceSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ashish.movieguide.R
import java.util.ArrayList

/**
 * Created by Ashish on Feb 08.
 */
val Menu.menus: List<MenuItem>
    get() = (0 until size()).map { getItem(it) }

fun Menu.changeMenuFont(typefaceSpan: TypefaceSpan) {
    menus.filterNotNull()
            .forEach { menuItem -> menuItem.title = menuItem.title.getTextWithCustomTypeface(typefaceSpan) }
}

fun Menu.tint(@ColorInt color: Int) = menus.forEach { it.tint(color) }

fun MenuItem?.tint(@ColorInt color: Int) = this?.icon.tint(color)

@SuppressLint("PrivateResource")
fun Activity.setOverflowMenuColor(color: Int) {
    val decorView = window.decorView as ViewGroup
    val overflowDescription = getString(R.string.abc_action_menu_overflow_description)

    decorView.onLayoutLaid {
        val views = ArrayList<View>()
        decorView.findViewsWithText(views, overflowDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION)
        if (views.isEmpty()) return@onLayoutLaid

        val overflow = views[0] as ImageView
        overflow.setColorFilter(color)
    }
}