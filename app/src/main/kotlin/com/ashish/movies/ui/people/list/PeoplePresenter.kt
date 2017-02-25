package com.ashish.movies.ui.people.list

import com.ashish.movies.data.interactors.PeopleInteractor
import com.ashish.movies.data.models.Person
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movies.utils.schedulers.BaseSchedulerProvider
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class PeoplePresenter @Inject constructor(
        private val peopleInteractor: PeopleInteractor,
        schedulerProvider: BaseSchedulerProvider
) : BaseRecyclerViewPresenter<Person, BaseRecyclerViewMvpView<Person>>(schedulerProvider) {

    override fun getResultsObservable(type: String?, page: Int) = peopleInteractor.getPopularPeople(page)
}