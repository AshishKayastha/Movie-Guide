package com.ashish.movies.ui.base.recyclerview

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.ViewGroup
import com.ashish.movies.ui.common.adapter.LoadingView
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.adapter.ViewTypeDelegateAdapter
import com.bumptech.glide.Glide
import java.util.*

/**
 * Created by Ashish on Dec 30.
 */
abstract class BaseRecyclerViewAdapter<in I : ViewType> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected var itemList: ArrayList<ViewType> = ArrayList()
    protected var delegateAdapters = SparseArray<ViewTypeDelegateAdapter>()

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

    fun showItemList(newItemList: List<I>?) {
        newItemList?.let { itemList.addAll(it) }
        notifyDataSetChanged()
    }

    fun addLoadingItem() {
        itemList.add(LoadingView())
        notifyItemInserted(itemCount - 1)
    }

    fun addNewItemList(newItemList: List<I>?) {
        val loadingItemPosition = removeLoadingItem()
        newItemList?.let { itemList.addAll(it) }
        notifyItemRangeChanged(loadingItemPosition, itemCount)
    }

    fun removeLoadingItem(): Int {
        val loadingItemPosition = itemCount - 1
        itemList.removeAt(loadingItemPosition)
        notifyItemRemoved(loadingItemPosition)
        notifyItemRangeChanged(loadingItemPosition, itemCount)
        return loadingItemPosition
    }
}