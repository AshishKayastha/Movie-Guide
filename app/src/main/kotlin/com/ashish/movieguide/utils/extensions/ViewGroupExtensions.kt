package com.ashish.movieguide.utils.extensions

import android.content.Context
import android.support.annotation.IdRes
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.TextView
import com.ashish.movieguide.ui.base.adapter.RecyclerViewItem
import com.ashish.movieguide.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movieguide.ui.widget.ItemOffsetDecoration
import com.ashish.movieguide.utils.FontUtils
import com.ashish.movieguide.utils.StartSnapHelper

operator fun ViewGroup?.get(position: Int): View? = this?.getChildAt(position)

val ViewGroup.views: List<View>
    get() = (0 until childCount).map { getChildAt(it) }

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun ViewGroup.changeViewGroupTextFont(boldFont: Boolean = false) {
    views.filterIsInstance<TextView>().forEach { it.changeTypeface(boldFont) }
}

fun TabLayout.changeTabFont(boldFont: Boolean = false) {
    val viewGroup = this[0] as ViewGroup
    (0 until viewGroup.childCount)
            .map { viewGroup[it] as ViewGroup }
            .forEach { it.changeViewGroupTextFont(boldFont) }
}

fun CollapsingToolbarLayout.changeTitleTypeface() {
    val regularFont = FontUtils.getTypeface(context)
    setExpandedTitleTypeface(regularFont)
    setCollapsedTitleTypeface(regularFont)
}

fun RecyclerView.ViewHolder.convertToFullSpan() {
    val params = itemView.layoutParams
    if (params is StaggeredGridLayoutManager.LayoutParams) {
        params.isFullSpan = true
        itemView.layoutParams = params
    }
}

fun ViewStub.inflateToRecyclerView(
        context: Context,
        @IdRes viewId: Int,
        recyclerAdapter: RecyclerView.Adapter<*>
): RecyclerView {
    val inflatedView = this.inflate()
    val recyclerView = inflatedView.find<RecyclerView>(viewId)

    recyclerView.apply {
        setHasFixedSize(true)
        addItemDecoration(ItemOffsetDecoration(7f.dpToPx().toInt()))
        layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        adapter = recyclerAdapter
        val snapHelper = StartSnapHelper()
        snapHelper.attachToRecyclerView(this)
    }

    return recyclerView
}

fun <I : RecyclerViewItem> ViewStub.inflateToRecyclerView(
        context: Context,
        @IdRes viewId: Int,
        adapter: RecyclerViewAdapter<I>,
        itemList: List<I>
): RecyclerView {
    val recyclerView = inflateToRecyclerView(context, viewId, adapter)
    adapter.showItemList(itemList)
    return recyclerView
}