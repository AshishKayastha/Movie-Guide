package com.ashish.movies.utils.extensions

import android.support.annotation.ColorInt
import android.text.style.TypefaceSpan
import android.view.Menu
import android.view.MenuItem

/**
 * Created by Ashish on Feb 08.
 */
val Menu.menus: List<MenuItem>
    get() = (0..size() - 1).map { getItem(it) }

fun Menu.changeMenuFont(typefaceSpan: TypefaceSpan) {
    menus.filterNotNull()
            .forEach { menuItem -> menuItem.title = menuItem.title.getTextWithCustomTypeface(typefaceSpan) }
}

fun Menu.tint(@ColorInt color: Int) = menus.forEach { it.tint(color) }

fun MenuItem?.tint(@ColorInt color: Int) = this?.icon.tint(color)