package com.ashish.movies.ui.people

import com.ashish.movies.data.interactors.PeopleInteractor
import com.ashish.movies.data.models.People
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movies.utils.Utils
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class PeoplePresenter @Inject constructor(val peopleInteractor: PeopleInteractor)
    : BaseRecyclerViewPresenter<People, BaseRecyclerViewMvpView<People>>() {

    override fun loadData(type: Int?, page: Int, showProgress: Boolean) {
        if (Utils.isOnline()) {
            getDataByType(null, page, showProgress)
        }
    }

    override fun loadMoreData(type: Int?, page: Int) {
        if (Utils.isOnline()) {
            if (page <= totalPages) {
                getMoreDataByType(null, page)
            }
        }
    }

    override fun getData(type: String?, page: Int) = peopleInteractor.getPopularPeople(page)
}