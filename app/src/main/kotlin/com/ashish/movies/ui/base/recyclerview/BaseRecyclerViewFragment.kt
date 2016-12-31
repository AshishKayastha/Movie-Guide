package com.ashish.movies.ui.base.recyclerview

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.ui.base.common.BaseFragment
import com.ashish.movies.ui.base.mvp.RxPresenter
import com.ashish.movies.ui.common.ViewType
import com.ashish.movies.ui.widget.EmptyRecyclerView
import com.ashish.movies.ui.widget.ItemOffsetDecoration
import com.ashish.movies.ui.widget.MultiSwipeRefreshLayout
import com.ashish.movies.utils.extensions.hide
import com.ashish.movies.utils.extensions.setVisibility
import com.ashish.movies.utils.extensions.show
import me.zhanghai.android.materialprogressbar.MaterialProgressBar

/**
 * Created by Ashish on Dec 30.
 */
abstract class BaseRecyclerViewFragment<I : ViewType, V : BaseRecyclerViewMvpView<I>, P : RxPresenter<V>>
    : BaseFragment<V, P>(), BaseRecyclerViewMvpView<I>, SwipeRefreshLayout.OnRefreshListener {

    val emptyContentView: View by bindView(R.id.empty_view)
    val recyclerView: EmptyRecyclerView by bindView(R.id.recycler_view)
    val progressBar: MaterialProgressBar by bindView(R.id.material_progress_bar)
    val swipeRefreshLayout: MultiSwipeRefreshLayout by bindView(R.id.swipe_refresh)

    lateinit var recyclerViewAdapter: BaseRecyclerViewAdapter<I>

    override fun getLayoutId() = R.layout.fragment_recycler_view

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        recyclerView.apply {
            setHasFixedSize(true)
            emptyView = emptyContentView
            addItemDecoration(ItemOffsetDecoration())
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = recyclerViewAdapter
        }

        swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.colorAccent)
            setSwipeableViews(emptyContentView, recyclerView)
            setOnRefreshListener(this@BaseRecyclerViewFragment)
        }
    }

    protected open fun initView() {}

    override fun onRefresh() {
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

    override fun showItemList(itemList: List<I>?) = recyclerViewAdapter.updateItemList(itemList)

    override fun addNewItems(itemList: List<I>?) = recyclerViewAdapter.addNewItems(itemList)

    override fun onDestroyView() {
        recyclerView.adapter = null
        swipeRefreshLayout.clearAnimation()
        recyclerView.clearOnScrollListeners()
        super.onDestroyView()
    }
}