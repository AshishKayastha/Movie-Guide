package com.ashish.movies.ui.base.recyclerview

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.ui.base.mvp.MvpFragment
import com.ashish.movies.ui.common.adapter.InfiniteScrollListener
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.widget.EmptyRecyclerView
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.ui.widget.ItemOffsetDecoration
import com.ashish.movies.ui.widget.MultiSwipeRefreshLayout
import com.ashish.movies.utils.Utils
import com.ashish.movies.utils.extensions.getPosterImagePair
import com.ashish.movies.utils.extensions.hide
import com.ashish.movies.utils.extensions.setVisibility
import com.ashish.movies.utils.extensions.show
import com.ashish.movies.utils.extensions.startActivityWithTransition
import icepick.State

/**
 * Created by Ashish on Dec 30.
 */
abstract class BaseRecyclerViewFragment<I : ViewType, V : BaseRecyclerViewMvpView<I>,
        P : BaseRecyclerViewPresenter<I, V>> : MvpFragment<V, P>(), BaseRecyclerViewMvpView<I>,
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    @JvmField @State var type: Int? = null

    protected val emptyContentView: View by bindView(R.id.empty_view)
    protected val progressBar: ProgressBar by bindView(R.id.progress_bar)
    protected val emptyTextView: FontTextView by bindView(R.id.empty_text)
    protected val emptyImageView: ImageView by bindView(R.id.empty_image_view)
    protected val recyclerView: EmptyRecyclerView by bindView(R.id.recycler_view)
    protected val swipeRefreshLayout: MultiSwipeRefreshLayout by bindView(R.id.swipe_refresh)

    protected lateinit var recyclerViewAdapter: RecyclerViewAdapter<I>

    protected val scrollListener: InfiniteScrollListener = object : InfiniteScrollListener() {
        override fun onLoadMore(currentPage: Int) {
            if (currentPage > 1) {
                recyclerView.post { presenter?.loadMoreData(type, currentPage) }
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_recycler_view

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    protected open fun initViews() {
        recyclerViewAdapter = RecyclerViewAdapter(R.layout.list_item_content, getAdapterType(), this)

        recyclerView.apply {
            setHasFixedSize(true)
            emptyView = emptyContentView
            addItemDecoration(ItemOffsetDecoration())
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            addOnScrollListener(scrollListener)
            adapter = recyclerViewAdapter
        }

        swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.colorAccent)
            setSwipeableViews(emptyContentView, recyclerView)
            setOnRefreshListener(this@BaseRecyclerViewFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isFirstStart) {
            loadData()
            isFirstStart = false
        }
    }

    protected open fun loadData() = presenter?.loadData(type)

    abstract fun getAdapterType(): Int

    override fun onRefresh() {
        scrollListener.resetPageCount()
        presenter?.loadFreshData(type, recyclerViewAdapter.itemCount == 0)
    }

    override fun showProgress() {
        emptyContentView.hide()
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
        swipeRefreshLayout.isRefreshing = false
        emptyContentView.setVisibility(recyclerViewAdapter.itemCount == 0)
    }

    override fun setCurrentPage(currentPage: Int) {
        scrollListener.setCurrentPage(currentPage)
    }

    override fun showItemList(itemList: List<I>?) = recyclerViewAdapter.showItemList(itemList)

    override fun showLoadingItem() = recyclerViewAdapter.addLoadingItem()

    override fun addNewItemList(itemList: List<I>?) = recyclerViewAdapter.addNewItemList(itemList)

    override fun removeLoadingItem() {
        recyclerViewAdapter.removeLoadingItem()
    }

    override fun resetLoading() = scrollListener.resetLoading()

    override fun onItemClick(position: Int, view: View) {
        if (Utils.isOnline()) {
            val intent = getDetailIntent(position)
            if (intent != null) {
                val posterImagePair = view.getPosterImagePair(getTransitionNameId(position))
                activity?.startActivityWithTransition(posterImagePair, intent)
            }
        } else {
            showMessage(R.string.error_no_internet)
        }
    }

    abstract fun getTransitionNameId(position: Int): Int

    abstract fun getDetailIntent(position: Int): Intent?

    override fun onDestroyView() {
        performCleanup()
        super.onDestroyView()
    }

    protected open fun performCleanup() {
        recyclerView.removeDataObserver()
        recyclerViewAdapter.removeListener()
        recyclerView.clearOnScrollListeners()
        recyclerView.adapter = null
        swipeRefreshLayout.clearAnimation()
        swipeRefreshLayout.setOnRefreshListener(null)
    }
}