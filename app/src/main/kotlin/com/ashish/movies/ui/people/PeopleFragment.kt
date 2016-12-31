package com.ashish.movies.ui.people

import com.ashish.movies.data.models.People
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter

/**
 * Created by Ashish on Dec 31.
 */
class PeopleFragment : BaseRecyclerViewFragment<People, BaseRecyclerViewMvpView<People>, PeoplePresenter>() {

    companion object {
        fun newInstance(): PeopleFragment {
            return PeopleFragment()
        }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(PeopleModule()).inject(this)
    }

    override fun initView() {
        recyclerViewAdapter = RecyclerViewAdapter(RecyclerViewAdapter.ADAPTER_TYPE_PEOPLE)
    }
}