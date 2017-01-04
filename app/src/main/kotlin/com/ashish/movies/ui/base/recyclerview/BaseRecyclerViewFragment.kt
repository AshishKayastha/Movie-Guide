package com.ashish.movies.ui.base.recyclerview

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.ImageView
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
import com.ashish.movies.utils.extensions.getActivityOptionsCompat
import com.ashish.movies.utils.extensions.getPosterImagePair
import com.ashish.movies.utils.extensions.hide
import com.ashish.movies.utils.extensions.setVisibility
import com.ashish.movies.utils.extensions.show
import me.zhanghai.android.materialprogressbar.MaterialProgressBar

/**
 * Created by Ashish on Dec 30.
 */
abstract class BaseRecyclerViewFragment<I : ViewType, V : BaseRecyclerViewMvpView<I>,
        P : BaseRecyclerViewPresenter<I, V>> : MvpFragment<V, P>(), BaseRecyclerViewMvpView<I>,
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    protected val emptyContentView: View by bindView(R.id.empty_view)
    protected val emptyTextView: FontTextView by bindView(R.id.empty_text)
    protected val emptyImageView: ImageView by bindView(R.id.empty_image_view)
    protected val recyclerView: EmptyRecyclerView by bindView(R.id.recycler_view)
    protected val progressBar: MaterialProgressBar by bindView(R.id.material_progress_bar)
    protected val swipeRefreshLayout: MultiSwipeRefreshLayout by bindView(R.id.swipe_refresh)

    protected lateinit var recyclerViewAdapter: RecyclerViewAdapter<I>

    protected var type: Int? = null

    protected val scrollListener: InfiniteScrollListener = object : InfiniteScrollListener() {
        override fun onLoadMore(currentPage: Int) {
            if (currentPage > 1) {
                recyclerView.post {
                    recyclerViewAdapter.addLoadingItem()
                    presenter.loadMoreData(type, currentPage)
                }
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_recycler_view

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFragmentArguments()

        recyclerViewAdapter = RecyclerViewAdapter(adapterType = getAdapterType(), onItemClickListener = this)
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

        presenter.loadData(type)
    }

    protected open fun getFragmentArguments() {}

    abstract fun getAdapterType(): Int

    override fun onRefresh() {
        scrollListener.resetPageCount()
        presenter.loadData(type, showProgress = recyclerViewAdapter.itemCount == 0)
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

    override fun showItemList(itemList: List<I>?) = recyclerViewAdapter.showItemList(itemList)

    override fun addNewItemList(itemList: List<I>?) = recyclerViewAdapter.addNewItemList(itemList)

    override fun removeLoadingItem() {
        recyclerViewAdapter.removeLoadingItem()
    }

    override fun resetLoading() = scrollListener.resetLoading()

    override fun onItemClick(position: Int, view: View) {
        val intent = getDetailIntent(position)
        if (intent != null) {
            val posterImagePair = view.getPosterImagePair(getString(R.string.transition_poster_image))
            val options = activity.getActivityOptionsCompat(posterImagePair)

            activity.window.exitTransition = null
            ActivityCompat.startActivity(activity, intent, options?.toBundle())
        }
    }

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