package com.ashish.movies.ui.moviedetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Movie
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.mvp.LceView
import com.ashish.movies.ui.base.mvp.MvpActivity
import com.ashish.movies.ui.common.palette.PaletteBitmap
import com.ashish.movies.utils.Constants.BACKDROP_W780_URL_PREFIX
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.transcodePaletteBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ImageViewTarget

/**
 * Created by Ashish on Dec 31.
 */
class MovieDetailActivity : MvpActivity<LceView, MovieDetailPresenter>(), LceView {

    val toolbar: Toolbar by bindView(R.id.toolbar)
    val backdropImage: ImageView by bindView(R.id.backdrop_image)

    private var movie: Movie? = null

    companion object {
        const val EXTRA_MOVIE = "movie"

        fun createIntent(context: Context, movie: Movie?): Intent {
            return Intent(context, MovieDetailActivity::class.java).putExtra(EXTRA_MOVIE, movie)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            movie = intent.getParcelableExtra(EXTRA_MOVIE)
        }

        showMovieBackdropImage()
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(MovieDetailModule()).inject(this)
    }

    override fun getLayoutId() = R.layout.activity_movie_detail

    fun showMovieBackdropImage() {
        val backdropPath = movie?.backdropPath
        if (backdropPath.isNotNullOrEmpty()) {
            Glide.with(this)
                    .transcodePaletteBitmap(this)
                    .load(BACKDROP_W780_URL_PREFIX + backdropPath)
                    .into(object : ImageViewTarget<PaletteBitmap>(backdropImage) {
                        override fun setResource(resource: PaletteBitmap?) {
                            super.view.setImageBitmap(resource?.bitmap)
                        }
                    })
        }
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }
}