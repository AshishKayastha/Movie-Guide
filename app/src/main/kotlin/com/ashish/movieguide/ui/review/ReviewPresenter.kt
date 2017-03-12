package com.ashish.movieguide.ui.review

import com.ashish.movieguide.data.interactors.MovieInteractor
import com.ashish.movieguide.data.models.tmdb.Results
import com.ashish.movieguide.data.models.tmdb.Review
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class ReviewPresenter @Inject constructor(
        private val movieInteractor: MovieInteractor,
        schedulerProvider: BaseSchedulerProvider
) : BaseRecyclerViewPresenter<Review, BaseRecyclerViewMvpView<Review>>(schedulerProvider) {

    private var movieId: Long = 0L

    fun setMovieId(movieId: Long) {
        this.movieId = movieId
    }

    override fun getResults(type: String?, page: Int): Single<Results<Review>>
            = movieInteractor.getMovieReviews(movieId, page)
}