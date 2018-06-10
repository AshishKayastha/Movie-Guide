package com.ashish.movieguide.ui.review

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.Review
import com.ashish.movieguide.ui.animation.SlideInUpAnimator
import com.ashish.movieguide.ui.base.mvp.MvpActivity
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.common.adapter.InfiniteScrollListener
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_REVIEW
import com.ashish.movieguide.utils.CustomTabsHelper
import com.ashish.movieguide.utils.extensions.changeViewGroupTextFont
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import javax.inject.Inject

class ReviewActivity : MvpActivity<RecyclerViewMvpView<Review>, ReviewPresenter>(),
        RecyclerViewMvpView<Review>, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    companion object {
        private const val EXTRA_MOVIE_ID = "movie_id"

        fun createIntent(context: Context, movieId: Long?): Intent {
            return Intent(context, ReviewActivity::class.java)
                    .putExtra(EXTRA_MOVIE_ID, movieId)
        }
    }

    @Inject lateinit var reviewPresenter: ReviewPresenter

    @State var movieId: Long = 0L

    private lateinit var reviewAdapter: RecyclerViewAdapter<Review>

    private val scrollListener: InfiniteScrollListener = InfiniteScrollListener { currentPage ->
        if (currentPage > 1) {
            reviewRecyclerView.post { reviewPresenter.loadMoreData(null, currentPage) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.slide_in_right, R.anim.hold_anim)
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.changeViewGroupTextFont()

        emptyText.setText(R.string.no_reviews_available)
        emptyImage.setImageResource(R.drawable.ic_review_white_100dp)

        reviewAdapter = RecyclerViewAdapter(R.layout.list_item_detail_review, ADAPTER_TYPE_REVIEW, this)

        reviewRecyclerView.apply {
            setHasFixedSize(true)
            emptyView = emptyContentView
            itemAnimator = SlideInUpAnimator()
            layoutManager = LinearLayoutManager(this@ReviewActivity)
            addOnScrollListener(scrollListener)
            adapter = reviewAdapter
        }

        swipeRefresh.apply {
            setSwipeableViews(emptyContentView, reviewRecyclerView)
            setOnRefreshListener(this@ReviewActivity)
        }
    }

    override fun getLayoutId() = R.layout.activity_review

    override fun providePresenter(): ReviewPresenter = reviewPresenter

    override fun getIntentExtras(extras: Bundle?) {
        movieId = extras?.getLong(EXTRA_MOVIE_ID) ?: 0L
    }

    override fun onStart() {
        super.onStart()
        reviewPresenter.setMovieId(movieId)
        reviewPresenter.loadData(null)
    }

    override fun onRefresh() {
        scrollListener.resetPageCount()
        reviewPresenter.fetchFreshData(null, false)
    }

    override fun setLoadingIndicator(showIndicator: Boolean) {
        swipeRefresh.isRefreshing = showIndicator
    }

    override fun setCurrentPage(currentPage: Int) {
        scrollListener.currentPage = currentPage
    }

    override fun showItemList(itemList: List<Review>?) = reviewAdapter.showItemList(itemList)

    override fun showLoadingItem() = reviewAdapter.addLoadingItem()

    override fun addNewItemList(itemList: List<Review>?) = reviewAdapter.addNewItemList(itemList)

    override fun removeLoadingItem() {
        reviewAdapter.removeLoadingItem()
    }

    override fun showErrorView() = reviewAdapter.addErrorItem()

    override fun resetLoading() = scrollListener.stopLoading()

    override fun onItemClick(position: Int, view: View) {
        val review = reviewAdapter.getItem<Review>(position)
        CustomTabsHelper.launchUrl(this, review.url)
    }

    override fun finishAfterTransition() {
        super.finishAfterTransition()
        overridePendingTransition(R.anim.hold_anim, R.anim.slide_out_right)
    }

    override fun onDestroy() {
        reviewAdapter.removeListener()
        super.onDestroy()
    }
}