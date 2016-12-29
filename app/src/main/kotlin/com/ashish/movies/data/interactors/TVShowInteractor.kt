package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.TVShowService
import com.ashish.movies.data.models.TVShowResults
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by Ashish on Dec 29.
 */
class TVShowInteractor @Inject constructor(val tvShowService: TVShowService) {

    fun getTVShowsByType(tvShowType: String, page: Int = 1): Observable<TVShowResults> {
        return tvShowService.getTVShows(tvShowType, page)
                .observeOn(AndroidSchedulers.mainThread())
    }
}