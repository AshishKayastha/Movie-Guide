package com.ashish.movieguide.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.ViewGroup
import com.ashish.movieguide.ui.base.recyclerview.BaseContentHolder
import com.ashish.movieguide.ui.common.adapter.ViewType.Companion.CONTENT_VIEW
import com.ashish.movieguide.ui.common.adapter.ViewType.Companion.LOADING_VIEW
import com.bumptech.glide.Glide
import java.util.ArrayList

/**
 * Created by Ashish on Dec 30.
 */
class RecyclerViewAdapter<in I : ViewType>(
        layoutId: Int,
        adapterType: Int,
        onItemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), RemoveListener {

    private val loadingItem = object : ViewType {
        override fun getViewType() = LOADING_VIEW
    }

    private var itemList: ArrayList<ViewType> = ArrayList()
    private var delegateAdapters = SparseArray<ViewTypeDelegateAdapter>()
    private val contentAdapter = AdapterFactory.getAdapter(layoutId, adapterType, onItemClickListener)

    init {
        delegateAdapters.put(LOADING_VIEW, LoadingDelegateAdapter())
        delegateAdapters.put(CONTENT_VIEW, contentAdapter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = delegateAdapters.get(viewType).onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, itemList[position])
    }

    override fun getItemViewType(position: Int) = itemList[position].getViewType()

    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
        super.onViewRecycled(holder)
        if (holder is BaseContentHolder<*>) Glide.clear(holder.posterImage)
    }

    override fun getItemCount() = itemList.size

    @Suppress("UNCHECKED_CAST")
    fun <I> getItem(position: Int) = itemList[position] as I

    fun showItemList(newItemList: List<I>?) {
        newItemList?.let {
            val oldPosition = itemCount
            itemList = ArrayList(it)
            notifyItemRangeInserted(oldPosition, it.size)
        }
    }

    fun addLoadingItem() {
        itemList.add(loadingItem)
        notifyItemInserted(itemCount - 1)
    }

    fun addNewItemList(newItemList: List<I>?) {
        val loadingItemPosition = removeLoadingItem()
        newItemList?.let {
            itemList.addAll(it)
            notifyItemRangeChanged(loadingItemPosition, itemCount)
        }
    }

    fun removeLoadingItem(): Int {
        val loadingItemPosition = itemCount - 1
        itemList.removeAt(loadingItemPosition)
        notifyItemRemoved(loadingItemPosition)
        notifyItemRangeChanged(loadingItemPosition, itemCount)
        return loadingItemPosition
    }

    fun clearAll() {
        val oldSize = itemCount
        if (oldSize > 0) {
            itemList.clear()
            notifyItemRangeRemoved(0, oldSize)
        }
    }

    override fun removeListener() = (contentAdapter as RemoveListener).removeListener()
}