package com.ashish.movies.ui.movies

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movies.data.models.Movie
import com.ashish.movies.ui.common.LoadingDelegateAdapter
import com.ashish.movies.ui.common.ViewType
import com.ashish.movies.ui.common.ViewType.Companion.CONTENT_VIEW
import com.ashish.movies.ui.common.ViewType.Companion.LOADING_VIEW
import com.ashish.movies.ui.common.ViewTypeDelegateAdapter
import java.util.*

/**
 * Created by Ashish on Dec 27.
 */
class MoviesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    private val loadingItem = object : ViewType {
        override fun getViewType() = LOADING_VIEW
    }

    init {
        delegateAdapters.put(LOADING_VIEW, LoadingDelegateAdapter())
        delegateAdapters.put(CONTENT_VIEW, MovieDelegateAdapter())
        itemList = ArrayList()
        itemList.add(loadingItem)
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

    fun updateMoviesList(newMoviesList: List<Movie>?) {
        itemList = ArrayList(newMoviesList)
        notifyDataSetChanged()
    }

    fun addMovieItems(newMoviesList: List<Movie>?) {
        val initPosition = itemList.size - 1
        itemList.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        newMoviesList?.let { itemList.addAll(it) }
        itemList.add(loadingItem)
        notifyItemRangeChanged(initPosition, itemList.size + 1)
    }
}