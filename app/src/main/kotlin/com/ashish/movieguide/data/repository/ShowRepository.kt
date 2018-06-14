package com.ashish.movieguide.data.repository

import com.ashish.movieguide.data.local.datasource.ShowLocalDataSource
import com.ashish.movieguide.data.local.entities.LocalShow
import com.ashish.movieguide.data.remote.interactors.TVShowInteractor
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowRepository @Inject constructor(
        private val tvShowInteractor: TVShowInteractor,
        private val showLocalDataSource: ShowLocalDataSource
) {

    fun getMovieDetail(showId: Long): Observable<LocalShow> {
        return tvShowInteractor.getFullTVShowDetail(showId)
                .flatMap { showLocalDataSource.putShowDetail(it) }
                .startWith { showLocalDataSource.getShow(showId) }
    }
}