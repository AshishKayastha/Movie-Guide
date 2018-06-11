package com.ashish.movieguide.ui.base.recyclerview

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.animation.SlideInUpAnimator
import com.ashish.movieguide.ui.base.adapter.RecyclerViewItem
import com.ashish.movieguide.ui.base.adapter.RecyclerViewItem.Companion.ERROR_VIEW
import com.ashish.movieguide.ui.base.mvp.MvpFragment
import com.ashish.movieguide.ui.common.adapter.InfiniteScrollListener
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movieguide.ui.widget.ItemOffsetDecoration
import com.ashish.movieguide.utils.extensions.getPosterImagePair
import com.ashish.movieguide.utils.extensions.startActivityWithTransition
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.fragment_recycler_view.*
import kotlinx.android.synthetic.main.layout_empty_view.*

abstract class RecyclerViewFragment<I : RecyclerViewItem, V : RecyclerViewMvpView<I>, P : RecyclerViewPresenter<I, V>>
    : MvpFragment<V, P>(), RecyclerViewMvpView<I>, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    @State var type: Int? = null

    protected lateinit var recyclerViewAdapter: RecyclerViewAdapter<I>

    private val scrollListener: InfiniteScrollListener = InfiniteScrollListener { currentPage ->
        if (currentPage > 1) recyclerView.post { presenter.loadMoreData(type, currentPage) }
    }

    override fun getLayoutId() = R.layout.fragment_recycler_view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyText.setText(getEmptyTextId())
        emptyImage.setImageResource(getEmptyImageId())

        recyclerViewAdapter = RecyclerViewAdapter(R.layout.list_item_content, getAdapterType(), this)

        recyclerView.apply {
            setHasFixedSize(true)
            emptyView = emptyContentView
            itemAnimator = SlideInUpAnimator()
            addItemDecoration(ItemOffsetDecoration())
            val columnCount = resources.getInteger(R.integer.content_column_count)
            layoutManager = StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL)
            addOnScrollListener(scrollListener)
            adapter = recyclerViewAdapter
        }

        swipeRefresh.apply {
            setSwipeableViews(emptyContentView, recyclerView)
            setOnRefreshListener(this@RecyclerViewFragment)
        }
    }

    abstract fun getEmptyTextId(): Int

    abstract fun getEmptyImageId(): Int

    abstract fun getAdapterType(): Int

    override fun onResume() {
        super.onResume()
        loadData()
    }

    protected open fun loadData() {
        presenter.loadData(type)
    }

    override fun onRefresh() {
        scrollListener.resetPageCount()
        presenter.fetchFreshData(type, false)
    }

    override fun setLoadingIndicator(showIndicator: Boolean) {
        swipeRefresh.isRefreshing = showIndicator
    }

    override fun setCurrentPage(currentPage: Int) {
        scrollListener.currentPage = currentPage
    }

    override fun showItemList(itemList: List<I>?) {
        recyclerViewAdapter.showItemList(itemList)
    }

    override fun showLoadingItem() {
        recyclerViewAdapter.addLoadingItem()
    }

    override fun addNewItemList(itemList: List<I>?) {
        recyclerViewAdapter.addNewItemList(itemList)
    }

    override fun removeLoadingItem() {
        recyclerViewAdapter.removeLoadingItem()
    }

    override fun showErrorView() {
        recyclerViewAdapter.addErrorItem()
    }

    override fun resetLoading() {
        scrollListener.stopLoading()
    }

    override fun onItemClick(position: Int, view: View) {
        val viewType = recyclerViewAdapter.getItemViewType(position)
        if (viewType == ERROR_VIEW) {
            recyclerViewAdapter.removeErrorItem()
            scrollListener.shouldLoadMore = true
            presenter.loadMoreData(type, scrollListener.currentPage)

        } else {
            val intent = if (activity != null) getDetailIntent(position) else null
            if (intent != null) {
                val posterImagePair = view.getPosterImagePair(getTransitionNameId(position))
                activity?.startActivityWithTransition(posterImagePair, intent)
            }
        }
    }

    abstract fun getDetailIntent(position: Int): Intent?

    abstract fun getTransitionNameId(position: Int): Int

    override fun onDestroyView() {
        recyclerViewAdapter.removeListener()
        super.onDestroyView()
    }
}