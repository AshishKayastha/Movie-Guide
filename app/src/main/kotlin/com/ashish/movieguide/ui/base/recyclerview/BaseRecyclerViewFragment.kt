package com.ashish.movieguide.ui.base.recyclerview

import android.content.Intent
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.animation.SlideInUpAnimator
import com.ashish.movieguide.ui.base.mvp.MvpFragment
import com.ashish.movieguide.ui.common.adapter.InfiniteScrollListener
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.ui.widget.ItemOffsetDecoration
import com.ashish.movieguide.ui.widget.MultiSwipeRefreshLayout
import com.ashish.movieguide.ui.widget.StaggeredGridRecyclerView
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.getPosterImagePair
import com.ashish.movieguide.utils.extensions.hide
import com.ashish.movieguide.utils.extensions.setVisibility
import com.ashish.movieguide.utils.extensions.show
import com.ashish.movieguide.utils.extensions.startActivityWithTransition
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
    protected val recyclerView: StaggeredGridRecyclerView by bindView(R.id.recycler_view)
    protected val swipeRefreshLayout: MultiSwipeRefreshLayout by bindView(R.id.swipe_refresh)

    protected lateinit var recyclerViewAdapter: RecyclerViewAdapter<I>

    private val scrollListener: InfiniteScrollListener = InfiniteScrollListener { currentPage ->
        if (currentPage > 1) recyclerView.post { presenter?.loadMoreData(type, currentPage) }
    }

    override fun getLayoutId() = R.layout.fragment_recycler_view

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        emptyTextView.setText(getEmptyTextId())
        emptyImageView.setImageResource(getEmptyImageId())

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

        swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.colorAccent)
            setSwipeableViews(emptyContentView, recyclerView)
            setOnRefreshListener(this@BaseRecyclerViewFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    protected open fun loadData() = presenter?.loadData(type)

    /**
     * Get type of adapter to represent type of data being shown in this list
     */
    abstract fun getAdapterType(): Int

    @StringRes
    abstract fun getEmptyTextId(): Int

    @DrawableRes
    abstract fun getEmptyImageId(): Int

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
        val intent = getDetailIntent(position)
        if (intent != null) {
            val posterImagePair = view.getPosterImagePair(getTransitionNameId(position))
            activity?.startActivityWithTransition(posterImagePair, intent)
        }
    }

    /**
     * Get transition name for shared element transition
     */
    abstract fun getTransitionNameId(position: Int): Int

    /**
     * Create intent to start detail activity
     */
    abstract fun getDetailIntent(position: Int): Intent?

    override fun onDestroyView() {
        recyclerViewAdapter.removeListener()
        super.onDestroyView()
    }
}