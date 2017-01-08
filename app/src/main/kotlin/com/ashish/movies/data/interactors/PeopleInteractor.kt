package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.PeopleApi
import com.ashish.movies.data.models.Person
import com.ashish.movies.data.models.PersonDetail
import com.ashish.movies.data.models.Results
import com.ashish.movies.utils.extensions.observeOnMainThread
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 31.
 */
@Singleton
class PeopleInteractor @Inject constructor(val peopleApi: PeopleApi) {

    fun getPopularPeople(page: Int = 1): Observable<Results<Person>> {
        return peopleApi.getPopularPeople(page).observeOnMainThread()
    }

    fun getPeopleDetailWithCombinedCredits(tvId: Long): Observable<PersonDetail> {
        return peopleApi.getPeopleDetailWithAppendedResponse(tvId, "combined_credits")
                .observeOnMainThread()
    }
}