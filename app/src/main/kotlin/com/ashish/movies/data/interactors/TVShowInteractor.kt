package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.TVShowApi
import com.ashish.movies.data.models.Results
import com.ashish.movies.data.models.TVShow
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 29.
 */
@Singleton
class TVShowInteractor @Inject constructor(val tvShowApi: TVShowApi) {

    fun getTVShowsByType(tvShowType: String?, page: Int = 1): Observable<Results<TVShow>> {
        return tvShowApi.getTVShows(tvShowType, page)
                .observeOn(AndroidSchedulers.mainThread())
    }
}