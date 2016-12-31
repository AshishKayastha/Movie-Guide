package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.PeopleService
import com.ashish.movies.data.models.People
import com.ashish.movies.data.models.Results
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class PeopleInteractor @Inject constructor(val peopleService: PeopleService) {

    fun getPopularPeople(page: Int = 1): Observable<Results<People>> {
        return peopleService.getPopularPeople(page)
                .observeOn(AndroidSchedulers.mainThread())
    }
}