package com.ashish.movies.ui.discover.movie

import com.ashish.movies.data.interactors.MovieInteractor
import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.Results
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewPresenter
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Ashish on Jan 06.
 */
class DiscoverMoviePresenter @Inject constructor(private val movieInteractor: MovieInteractor)
    : BaseRecyclerViewPresenter<Movie, BaseRecyclerViewMvpView<Movie>>() {

    private var year: Int = 2016
    private var genres: String? = null
    private var sortBy: String = "popularity.desc"

    fun setFilterQueries(sortBy: String, year: Int, genres: String) {
        this.sortBy = sortBy
        this.year = year
        this.genres = genres
    }

    override fun getType(type: Int?) = null

    override fun getResultsObservable(type: String?, page: Int): Observable<Results<Movie>> {
        return movieInteractor.discoverMovie(sortBy, year, genres, page)
    }
}