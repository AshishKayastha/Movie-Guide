package com.ashish.movies.ui.moviedetail

import com.ashish.movies.data.interactors.MovieInteractor
import com.ashish.movies.ui.base.mvp.LceView
import com.ashish.movies.ui.base.mvp.RxPresenter
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class MovieDetailPresenter @Inject constructor(movieInteractor: MovieInteractor) : RxPresenter<LceView>() {

}