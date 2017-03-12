package com.ashish.movieguide.ui.movie.detail

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.view.menu.ActionMenuItemView
import android.view.MenuItem
import android.view.View
import android.view.ViewStub
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.tmdb.Movie
import com.ashish.movieguide.data.models.tmdb.MovieDetail
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentActivity
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movieguide.ui.common.rating.RatingDialog
import com.ashish.movieguide.ui.review.ReviewActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MOVIE
import com.ashish.movieguide.utils.Constants.TMDB_URL
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.changeWatchlistMenuItem
import com.ashish.movieguide.utils.extensions.getBackdropUrl
import com.ashish.movieguide.utils.extensions.getPosterUrl
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.setFavoriteIcon
import com.ashish.movieguide.utils.extensions.setRatingItemTitle
import com.ashish.movieguide.utils.extensions.setTitleAndYear
import com.ashish.movieguide.utils.extensions.startFavoriteAnimation
import dagger.Lazy
import icepick.State
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class MovieDetailActivity : FullDetailContentActivity<MovieDetail,
        MovieDetailView, MovieDetailPresenter>(),
        MovieDetailView, RatingDialog.UpdateRatingListener {

    companion object {
        private const val EXTRA_MOVIE = "movie"

        @JvmStatic
        fun createIntent(context: Context, movie: Movie?): Intent {
            return Intent(context, MovieDetailActivity::class.java)
                    .putExtra(EXTRA_MOVIE, movie)
        }
    }

    @Inject lateinit var ratingDialog: Lazy<RatingDialog>
    @Inject lateinit var preferenceHelper: PreferenceHelper

    @JvmField @State var movie: Movie? = null

    private val readReviewsView: View by bindView(R.id.read_reviews_view)
    private val similarMoviesViewStub: ViewStub by bindView(R.id.similar_content_view_stub)

    private var similarMoviesAdapter: RecyclerViewAdapter<Movie>? = null

    private val isLoggedIn: Boolean by lazy {
        preferenceHelper.getId() > 0
    }

    private val onSimilarMovieItemClickLitener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            val movie = similarMoviesAdapter?.getItem<Movie>(position)
            val intent = createIntent(this@MovieDetailActivity, movie)
            startNewActivityWithTransition(view, R.string.transition_movie_poster, intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        readReviewsView.setOnClickListener {
            startActivity(ReviewActivity.createIntent(this, movie?.id))
        }
    }

    override fun injectDependencies(builderHost: ActivityComponentBuilderHost) {
        builderHost.getActivityComponentBuilder(MovieDetailActivity::class.java,
                MovieDetailComponent.Builder::class.java)
                .withModule(ActivityModule(this))
                .build()
                .inject(this)
    }

    override fun getLayoutId() = R.layout.activity_detail_movie

    override fun getIntentExtras(extras: Bundle?) {
        movie = extras?.getParcelable(EXTRA_MOVIE)
    }

    override fun getTransitionNameId() = R.string.transition_movie_poster

    override fun loadDetailContent() {
        presenter?.loadDetailContent(movie?.id)
    }

    override fun getBackdropPath() = movie?.backdropPath.getBackdropUrl()

    override fun getPosterPath() = movie?.posterPath.getPosterUrl()

    override fun getItemTitle(): String = movie?.title ?: ""

    override fun showDetailContent(detailContent: MovieDetail) {
        detailContent.apply {
            if (getBackdropPath().isNullOrEmpty() && backdropPath.isNotNullOrEmpty()) {
                showBackdropImage(backdropPath.getBackdropUrl())
            }

            this@MovieDetailActivity.imdbId = imdbId
            titleText.setTitleAndYear(title, releaseDate)
        }

        if (isLoggedIn) {
            ratingDialog.get().setRatingListener(this)
            menu?.setRatingItemTitle(R.string.title_rate_movie)
        }

        super.showDetailContent(detailContent)
    }

    override fun getDetailContentType() = ADAPTER_TYPE_MOVIE

    override fun showSimilarMoviesList(similarMoviesList: List<Movie>) {
        similarMoviesAdapter = RecyclerViewAdapter(R.layout.list_item_content_alt, ADAPTER_TYPE_MOVIE,
                onSimilarMovieItemClickLitener)

        inflateViewStubRecyclerView(similarMoviesViewStub, R.id.similar_content_recycler_view,
                similarMoviesAdapter!!, similarMoviesList)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> performAction { presenter?.markAsFavorite() }
        R.id.action_watchlist -> performAction { presenter?.addToWatchlist() }

        R.id.action_rating -> performAction {
            if (isLoggedIn) {
                ratingDialog.get().showRatingDialog(ratingLabelLayout.getRating())
            }
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun getShareText(): CharSequence {
        return movie!!.title + "\n\n" + TMDB_URL + "movie/" + movie!!.id
    }

    override fun setFavoriteIcon(isFavorite: Boolean) {
        menu?.setFavoriteIcon(isFavorite)
    }

    override fun animateFavoriteIcon(isFavorite: Boolean) {
        val view = toolbar?.findViewById(R.id.action_favorite)
        if (view is ActionMenuItemView) {
            view.startFavoriteAnimation(isFavorite)
        }
    }

    override fun changeWatchlistMenuItem(isInWatchlist: Boolean) {
        menu?.changeWatchlistMenuItem(isInWatchlist)
    }

    override fun showSavedRating(rating: Double?) {
        ratingLabelLayout.setRating(rating)
    }

    override fun saveRating(rating: Double) {
        presenter?.saveRating(rating)
    }

    override fun deleteRating() {
        presenter?.deleteRating()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        if (isLoggedIn) ratingDialog.get().dismissDialog()
        super.onConfigurationChanged(newConfig)
    }

    override fun onStop() {
        if (isLoggedIn) ratingDialog.get().dismissDialog()
        super.onStop()
    }

    override fun performCleanup() {
        super.performCleanup()
        similarMoviesAdapter?.removeListener()
        if (isLoggedIn) ratingDialog.get().setRatingListener(null)
    }
}