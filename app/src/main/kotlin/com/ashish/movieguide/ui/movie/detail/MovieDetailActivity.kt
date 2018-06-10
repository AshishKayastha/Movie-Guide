package com.ashish.movieguide.ui.movie.detail

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.view.menu.ActionMenuItemView
import android.view.MenuItem
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.Movie
import com.ashish.movieguide.data.network.entities.tmdb.MovieDetail
import com.ashish.movieguide.data.network.entities.trakt.TraktMovie
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentActivity
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movieguide.ui.common.rating.RatingDialog
import com.ashish.movieguide.ui.review.ReviewActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MOVIE
import com.ashish.movieguide.utils.TMDbConstants.TMDB_URL
import com.ashish.movieguide.utils.extensions.changeWatchlistMenuItem
import com.ashish.movieguide.utils.extensions.find
import com.ashish.movieguide.utils.extensions.getBackdropUrl
import com.ashish.movieguide.utils.extensions.getPosterUrl
import com.ashish.movieguide.utils.extensions.inflateToRecyclerView
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.performAction
import com.ashish.movieguide.utils.extensions.setFavoriteIcon
import com.ashish.movieguide.utils.extensions.setRatingItemTitle
import com.ashish.movieguide.utils.extensions.setTitleAndYear
import com.ashish.movieguide.utils.extensions.show
import com.ashish.movieguide.utils.extensions.startFavoriteAnimation
import com.evernote.android.state.State
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.layout_detail_app_bar.*
import kotlinx.android.synthetic.main.layout_detail_read_reviews.*
import kotlinx.android.synthetic.main.layout_detail_similar_content_stub.*
import javax.inject.Inject

class MovieDetailActivity : FullDetailContentActivity<MovieDetail, TraktMovie,
        MovieDetailView, MovieDetailPresenter>(), MovieDetailView, RatingDialog.UpdateRatingListener {

    companion object {
        private const val EXTRA_MOVIE = "movie"

        fun createIntent(context: Context, movie: Movie?): Intent {
            return Intent(context, MovieDetailActivity::class.java)
                    .putExtra(EXTRA_MOVIE, movie)
        }
    }

    @Inject lateinit var ratingDialog: Lazy<RatingDialog>
    @Inject lateinit var preferenceHelper: PreferenceHelper
    @Inject lateinit var movieDetailPresenter: MovieDetailPresenter

    @State var movie: Movie? = null

    private var similarMoviesAdapter: RecyclerViewAdapter<Movie>? = null
    private val isLoggedIn: Boolean by lazy { preferenceHelper.isLoggedIn() }

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

    override fun getLayoutId() = R.layout.activity_detail_movie

    override fun providePresenter(): MovieDetailPresenter = movieDetailPresenter

    override fun getIntentExtras(extras: Bundle?) {
        movie = extras?.getParcelable(EXTRA_MOVIE)
    }

    override fun getTransitionNameId() = R.string.transition_movie_poster

    override fun loadDetailContent() {
        movieDetailPresenter.loadDetailContent(movie?.id)
    }

    override fun getBackdropPath(): String = movie?.backdropPath.getBackdropUrl()

    override fun getPosterPath(): String = movie?.posterPath.getPosterUrl()

    override fun getItemTitle(): String = movie?.title ?: ""

    override fun showDetailContent(detailContent: MovieDetail) {
        detailContent.apply {
            if (getBackdropPath().isEmpty() && backdropPath.isNotNullOrEmpty()) {
                showBackdropImage(backdropPath.getBackdropUrl())
            }

            this@MovieDetailActivity.imdbId = imdbId
            contentTitleText.setTitleAndYear(title, releaseDate)
        }

        performActionIfLoggedIn {
            it.setRatingListener(this)
            menu?.setRatingItemTitle(R.string.title_rate_movie)
        }

        detailMovieContainer.show()
        super.showDetailContent(detailContent)
    }

    override fun getDetailContentType() = ADAPTER_TYPE_MOVIE

    override fun showSimilarMoviesList(similarMoviesList: List<Movie>) {
        similarMoviesAdapter = RecyclerViewAdapter(
                R.layout.list_item_content_alt,
                ADAPTER_TYPE_MOVIE,
                onSimilarMovieItemClickLitener
        )

        similarContentViewStub.inflateToRecyclerView(this,
                R.id.similarContentRecyclerView,
                similarMoviesAdapter!!,
                similarMoviesList
        )
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> performAction { movieDetailPresenter.markAsFavorite() }
        R.id.action_watchlist -> performAction { movieDetailPresenter.addToWatchlist() }

        R.id.action_rating -> performAction {
            performActionIfLoggedIn { it.showRatingDialog(myRatingLabel.getRating()) }
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun getShareText(): CharSequence {
        return "${movie!!.title}\n\n${TMDB_URL}movie/${movie!!.id}"
    }

    override fun setFavoriteIcon(isFavorite: Boolean) {
        menu?.setFavoriteIcon(isFavorite)
    }

    override fun animateFavoriteIcon(isFavorite: Boolean) {
        val view = toolbar?.find<View>(R.id.action_favorite)
        if (view is ActionMenuItemView) {
            view.startFavoriteAnimation(isFavorite)
        }
    }

    override fun changeWatchlistMenuItem(isInWatchlist: Boolean) {
        menu?.changeWatchlistMenuItem(isInWatchlist)
    }

    override fun showSavedRating(rating: Int?) {
        myRatingLabel.setRating(rating)
    }

    override fun addRating(rating: Int) {
        movieDetailPresenter.addRating(rating)
    }

    override fun removeRating() {
        movieDetailPresenter.removeRating()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        dismissDialog()
        super.onConfigurationChanged(newConfig)
    }

    override fun onStop() {
        dismissDialog()
        super.onStop()
    }

    private fun dismissDialog() {
        performActionIfLoggedIn { if (isFinishing) it.dismissDialog() }
    }

    private fun performActionIfLoggedIn(action: (RatingDialog) -> Unit) {
        if (isLoggedIn) action(ratingDialog.get())
    }

    override fun performCleanup() {
        super.performCleanup()
        similarMoviesAdapter?.removeListener()
        performActionIfLoggedIn { it.setRatingListener(null) }
    }
}