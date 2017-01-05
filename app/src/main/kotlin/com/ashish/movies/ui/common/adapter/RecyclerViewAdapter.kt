package com.ashish.movies.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.ViewGroup
import com.ashish.movies.R
import com.ashish.movies.ui.base.recyclerview.BaseContentHolder
import com.ashish.movies.ui.common.adapter.ViewType.Companion.CONTENT_VIEW
import com.ashish.movies.ui.common.adapter.ViewType.Companion.LOADING_VIEW
import com.ashish.movies.ui.movie.list.MovieDelegateAdapter
import com.ashish.movies.ui.people.list.PeopleDelegateAdapter
import com.ashish.movies.ui.tvshow.detail.SeasonDelegateAdapter
import com.ashish.movies.ui.tvshow.list.TVShowDelegateAdapter
import com.bumptech.glide.Glide
import java.util.*

/**
 * Created by Ashish on Dec 30.
 */
class RecyclerViewAdapter<in I : ViewType>(val layoutId: Int = R.layout.list_item_content, val adapterType: Int,
                                           onItemClickListener: OnItemClickListener?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), RemoveListener {

    companion object {
        const val ADAPTER_TYPE_MOVIE = 0
        const val ADAPTER_TYPE_TV_SHOW = 1
        const val ADAPTER_TYPE_PEOPLE = 2
        const val ADAPTER_TYPE_CREDIT = 3
        const val ADAPTER_TYPE_SEASON = 4
    }

    private val DELEGATE_ADAPTERS = arrayOf(
            MovieDelegateAdapter(layoutId, onItemClickListener),
            TVShowDelegateAdapter(layoutId, onItemClickListener),
            PeopleDelegateAdapter(layoutId, onItemClickListener),
            CreditDelegateAdapter(layoutId, onItemClickListener),
            SeasonDelegateAdapter(layoutId, onItemClickListener))

    private var itemList: ArrayList<ViewType> = ArrayList()
    private var delegateAdapters = SparseArray<ViewTypeDelegateAdapter>()

    init {
        delegateAdapters.put(LOADING_VIEW, LoadingDelegateAdapter())
        delegateAdapters.put(CONTENT_VIEW, DELEGATE_ADAPTERS[adapterType] as ViewTypeDelegateAdapter)
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

    override fun removeListener() = (DELEGATE_ADAPTERS[adapterType] as RemoveListener).removeListener()
}