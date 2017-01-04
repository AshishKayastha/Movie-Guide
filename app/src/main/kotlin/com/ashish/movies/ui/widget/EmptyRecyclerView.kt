package com.ashish.movies.ui.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.ashish.movies.utils.extensions.setVisibility

/**
 * Created by Ashish on Dec 27.
 */
class EmptyRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RecyclerView(context, attrs, defStyleAttr) {

    var emptyView: View? = null

    private val dataObserver: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            showEmptyView()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            showEmptyView()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            showEmptyView()
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        if (adapter != null) {
            adapter.registerAdapterDataObserver(dataObserver)
            dataObserver.onChanged()
        }
    }

    private fun showEmptyView() {
        val hasItems = adapter.itemCount > 0
        setVisibility(hasItems)
        emptyView?.setVisibility(!hasItems)
    }

    fun removeDataObserver() = adapter?.unregisterAdapterDataObserver(dataObserver)
}