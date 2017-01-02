package com.ashish.movies.ui.people

import com.ashish.movies.data.interactors.PeopleInteractor
import com.ashish.movies.data.models.People
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewPresenter
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class PeoplePresenter @Inject constructor(val peopleInteractor: PeopleInteractor)
    : BaseRecyclerViewPresenter<People, BaseRecyclerViewMvpView<People>>() {

    override fun getType(type: Int?) = null

    override fun getResultsObservable(type: String?, page: Int) = peopleInteractor.getPopularPeople(page)
}