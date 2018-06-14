package com.ashish.movieguide.ui.people.list

import com.ashish.movieguide.data.remote.entities.tmdb.Person
import com.ashish.movieguide.data.remote.entities.tmdb.Results
import com.ashish.movieguide.data.remote.interactors.PeopleInteractor
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewPresenter
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class PeoplePresenter @Inject constructor(
        private val peopleInteractor: PeopleInteractor,
        schedulerProvider: BaseSchedulerProvider
) : RecyclerViewPresenter<Person, RecyclerViewMvpView<Person>>(schedulerProvider) {

    override fun getResults(type: String?, page: Int): Single<Results<Person>>
            = peopleInteractor.getPopularPeople(page)
}