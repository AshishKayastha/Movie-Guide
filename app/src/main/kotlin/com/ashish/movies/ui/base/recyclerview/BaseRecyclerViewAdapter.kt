package com.ashish.movies.ui.base.recyclerview

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.ViewGroup
import com.ashish.movies.ui.common.ViewType
import com.ashish.movies.ui.common.ViewTypeDelegateAdapter
import java.util.*

/**
 * Created by Ashish on Dec 30.
 */
abstract class BaseRecyclerViewAdapter<in I : ViewType> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected var itemList: ArrayList<ViewType> = ArrayList()
    protected var delegateAdapters = SparseArray<ViewTypeDelegateAdapter>()

    protected val loadingItem = object : ViewType {
        override fun getViewType() = ViewType.LOADING_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegateAdapters.get(viewType).onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, itemList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position].getViewType()
    }

    override fun getItemCount(): Int = itemList.size

    fun updateItemList(newItemList: List<I>?) {
        itemList = ArrayList(newItemList)
        notifyDataSetChanged()
    }

    fun addNewItems(newItemList: List<I>?) {
        val initPosition = itemList.size - 1
        itemList.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        newItemList?.let { itemList.addAll(it) }
        itemList.add(loadingItem)
        notifyItemRangeChanged(initPosition, itemList.size + 1)
    }
}