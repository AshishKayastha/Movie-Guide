package com.ashish.movieguide.data.repository.models

import com.ashish.movieguide.data.database.entities.Show
import com.ashish.movieguide.data.interactors.TVShowInteractor
import com.ashish.movieguide.data.repository.local.LocalShowRepository
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowModel @Inject constructor(
        private val tvShowInteractor: TVShowInteractor,
        private val localShowRepository: LocalShowRepository
) {

    fun getMovieDetail(showId: Long): Observable<Show> {
        return tvShowInteractor.getFullTVShowDetail(showId)
                .flatMap { localShowRepository.putShowDetail(it) }
                .startWith { localShowRepository.getShow(showId) }
    }
}