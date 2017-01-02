package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.PeopleApi
import com.ashish.movies.data.models.People
import com.ashish.movies.data.models.Results
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class PeopleInteractor @Inject constructor(val peopleApi: PeopleApi) {

    fun getPopularPeople(page: Int = 1): Observable<Results<People>> {
        return peopleApi.getPopularPeople(page)
                .observeOn(AndroidSchedulers.mainThread())
    }
}