package com.ashish.movies.ui.moviedetail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.widget.NestedScrollView
import android.widget.ImageView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Movie
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.mvp.LceView
import com.ashish.movies.ui.base.mvp.MvpActivity
import com.ashish.movies.ui.common.palette.PaletteBitmap
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.Constants.BACKDROP_W780_URL_PREFIX
import com.ashish.movies.utils.Constants.POSTER_W500_URL_PREFIX
import com.ashish.movies.utils.FontUtils
import com.ashish.movies.utils.extensions.animateBackgroundColorChange
import com.ashish.movies.utils.extensions.animateTextColorChange
import com.ashish.movies.utils.extensions.dpToPx
import com.ashish.movies.utils.extensions.getColorCompat
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.setPaletteColor
import com.ashish.movies.utils.extensions.transcodePaletteBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ImageViewTarget
import timber.log.Timber


/**
 * Created by Ashish on Dec 31.
 */
class MovieDetailActivity : MvpActivity<LceView, MovieDetailPresenter>(), LceView, NestedScrollView.OnScrollChangeListener {

    val backdropImage: ImageView by bindView(R.id.backdrop_image)
    val movieTitle: FontTextView by bindView(R.id.movie_title_text)
    val moviePosterImage: ImageView by bindView(R.id.movie_poster_image)
    val nestedScrollView: NestedScrollView by bindView(R.id.nested_scroll_view)
    val collapsingToolbar: CollapsingToolbarLayout by bindView(R.id.collapsing_toolbar)

    private var movie: Movie? = null
    private var scrollThreshold: Int = 0

    private val regularFont: Typeface by lazy(LazyThreadSafetyMode.NONE) {
        FontUtils.getTypeface(this, FontUtils.MONTSERRAT_REGULAR)
    }

    private val primaryTextColor by lazy(LazyThreadSafetyMode.NONE) {
        getColorCompat(R.color.primary_text_light)
    }

    companion object {
        const val EXTRA_MOVIE = "movie"

        @JvmStatic val POSTER_HEIGHT = 156f.dpToPx()
        @JvmStatic val TITLE_PADDING = 16f.dpToPx()

        fun createIntent(context: Context, movie: Movie?): Intent {
            return Intent(context, MovieDetailActivity::class.java).putExtra(EXTRA_MOVIE, movie)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            movie = intent.getParcelableExtra(EXTRA_MOVIE)
        }

        showMoviePosterImage()
        showMovieBackdropImage()
        nestedScrollView.setOnScrollChangeListener(this)

        movieTitle.text = movie?.title ?: ""
        collapsingToolbar.setCollapsedTitleTypeface(regularFont)
        collapsingToolbar.title = ""
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

    fun showMoviePosterImage() {
        val posterPath = movie?.posterPath
        if (posterPath.isNotNullOrEmpty()) {
            Glide.with(this)
                    .transcodePaletteBitmap(this)
                    .load(POSTER_W500_URL_PREFIX + posterPath)
                    .into(object : ImageViewTarget<PaletteBitmap>(moviePosterImage) {
                        override fun setResource(resource: PaletteBitmap?) {
                            super.view.setImageBitmap(resource?.bitmap)

                            resource.setPaletteColor { swatch ->
                                movieTitle.animateBackgroundColorChange(Color.TRANSPARENT, swatch.rgb)
                                movieTitle.animateTextColorChange(primaryTextColor, swatch.bodyTextColor)
                            }
                        }
                    })
        }
    }

    override fun onScrollChange(v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        scrollThreshold = (POSTER_HEIGHT - movieTitle.measuredHeight).toInt()
        Timber.v(scrollThreshold.toString())
        Timber.v(scrollY.toString())
        collapsingToolbar.title = if (scrollY >= scrollThreshold) movie?.title ?: "" else ""
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }
}