package com.ashish.movieguide.data.interactors

import com.ashish.movieguide.data.network.api.tmdb.PeopleApi
import com.ashish.movieguide.data.network.entities.common.FullDetailContent
import com.ashish.movieguide.data.network.entities.tmdb.Person
import com.ashish.movieguide.data.network.entities.tmdb.PersonDetail
import com.ashish.movieguide.data.network.entities.tmdb.Results
import com.ashish.movieguide.data.network.entities.trakt.TraktPerson
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 31.
 */
@Singleton
class PeopleInteractor @Inject constructor(private val peopleApi: PeopleApi) {

    fun getPopularPeople(page: Int = 1): Single<Results<Person>> {
        return peopleApi.getPopularPeople(page)
    }

    fun getFullPersonDetail(personId: Long): Observable<FullDetailContent<PersonDetail, TraktPerson>> {
        return peopleApi.getPersonDetail(personId, "combined_credits,images")
                .flatMap { Observable.just(FullDetailContent(it, null, null)) }
    }
}