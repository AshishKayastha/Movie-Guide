package com.ashish.movieguide.data.interactors

import com.ashish.movieguide.data.api.tmdb.PeopleApi
import com.ashish.movieguide.data.models.common.FullDetailContent
import com.ashish.movieguide.data.models.tmdb.Person
import com.ashish.movieguide.data.models.tmdb.PersonDetail
import com.ashish.movieguide.data.models.tmdb.Results
import com.ashish.movieguide.data.models.trakt.TraktPerson
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

    fun getFullPersonDetail(personId: Long): Single<FullDetailContent<PersonDetail, TraktPerson>> {
        return peopleApi.getPersonDetail(personId, "combined_credits,images")
                .flatMap { Single.just(FullDetailContent(it, null, null)) }
    }
}