package com.ashish.movieguide.ui.review

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.tmdb.Review
import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movieguide.ui.animation.SlideInUpAnimator
import com.ashish.movieguide.ui.base.mvp.MvpActivity
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.common.adapter.InfiniteScrollListener
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_REVIEW
import com.ashish.movieguide.utils.CustomTabsHelper
import com.ashish.movieguide.utils.extensions.changeViewGroupTextFont
import com.ashish.movieguide.utils.extensions.hide
import com.ashish.movieguide.utils.extensions.setVisibility
import com.ashish.movieguide.utils.extensions.show
import icepick.State
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import kotlinx.android.synthetic.main.layout_progress_bar.*

class ReviewActivity : MvpActivity<BaseRecyclerViewMvpView<Review>, ReviewPresenter>(),
        BaseRecyclerViewMvpView<Review>, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    companion object {
        private const val EXTRA_MOVIE_ID = "movie_id"

        @JvmStatic
        fun createIntent(context: Context, movieId: Long?): Intent {
            return Intent(context, ReviewActivity::class.java)
                    .putExtra(EXTRA_MOVIE_ID, movieId)
        }
    }

    @JvmField @State var movieId: Long = 0L

    private lateinit var reviewAdapter: RecyclerViewAdapter<Review>

    private val scrollListener: InfiniteScrollListener = InfiniteScrollListener { currentPage ->
        if (currentPage > 1) reviewRecyclerView.post { presenter?.loadMoreData(null, currentPage) }
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
            setColorSchemeResources(R.color.colorAccent)
            setSwipeableViews(emptyContentView, reviewRecyclerView)
            setOnRefreshListener(this@ReviewActivity)
        }
    }

    override fun injectDependencies(builderHost: ActivityComponentBuilderHost) {
        builderHost.getActivityComponentBuilder(ReviewActivity::class.java,
                ReviewComponent.Builder::class.java)
                .withModule(ActivityModule(this))
                .build()
                .inject(this)
    }

    override fun getLayoutId() = R.layout.activity_review

    override fun getIntentExtras(extras: Bundle?) {
        movieId = extras?.getLong(EXTRA_MOVIE_ID) ?: 0L
    }

    override fun onStart() {
        super.onStart()
        presenter?.setMovieId(movieId)
        presenter?.loadData(null)
    }

    override fun onRefresh() {
        scrollListener.resetPageCount()
        presenter?.loadFreshData(null, reviewAdapter.itemCount == 0)
    }

    override fun showProgress() {
        emptyContentView.hide()
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
        swipeRefresh.isRefreshing = false
        emptyContentView.setVisibility(reviewAdapter.itemCount == 0)
    }

    override fun setCurrentPage(currentPage: Int) {
        scrollListener.setCurrentPage(currentPage)
    }

    override fun showItemList(itemList: List<Review>?) = reviewAdapter.showItemList(itemList)

    override fun showLoadingItem() = reviewAdapter.addLoadingItem()

    override fun addNewItemList(itemList: List<Review>?) = reviewAdapter.addNewItemList(itemList)

    override fun removeLoadingItem() {
        reviewAdapter.removeLoadingItem()
    }

    override fun resetLoading() = scrollListener.resetLoading()

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