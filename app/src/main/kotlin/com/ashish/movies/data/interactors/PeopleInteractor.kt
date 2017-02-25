package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.PeopleApi
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.Person
import com.ashish.movies.data.models.PersonDetail
import com.ashish.movies.data.models.Results
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 31.
 */
@Singleton
class PeopleInteractor @Inject constructor(private val peopleApi: PeopleApi) {

    fun getPopularPeople(page: Int = 1): Observable<Results<Person>> {
        return peopleApi.getPopularPeople(page)
    }

    fun getFullPersonDetail(personId: Long): Observable<FullDetailContent<PersonDetail>> {
        return peopleApi.getPersonDetail(personId, "combined_credits,images")
                .flatMap { Observable.just(FullDetailContent(it, null)) }
    }
}