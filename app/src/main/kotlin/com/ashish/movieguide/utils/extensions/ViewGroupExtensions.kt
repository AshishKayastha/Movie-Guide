package com.ashish.movieguide.utils.extensions

import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

operator fun ViewGroup?.get(position: Int): View? = this?.getChildAt(position)

val ViewGroup.views: List<View>
    get() = (0 until childCount).map { getChildAt(it) }

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View? {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun ViewGroup.changeViewGroupTextFont() {
    views.filterIsInstance<TextView>().forEach { it.changeTypeface() }
}

fun TabLayout.changeTabFont() {
    val viewGroup = this[0] as ViewGroup
    (0 until viewGroup.childCount)
            .map { viewGroup[it] as ViewGroup }
            .forEach { it.changeViewGroupTextFont() }
}