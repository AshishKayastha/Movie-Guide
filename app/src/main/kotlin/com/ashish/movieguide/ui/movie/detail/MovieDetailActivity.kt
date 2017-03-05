package com.ashish.movieguide.ui.movie.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.view.menu.ActionMenuItemView
import android.view.MenuItem
import android.view.View
import android.view.ViewStub
import butterknife.bindView
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.Movie
import com.ashish.movieguide.data.models.MovieDetail
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentActivity
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MOVIE
import com.ashish.movieguide.utils.extensions.getBackdropUrl
import com.ashish.movieguide.utils.extensions.getPosterUrl
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.setFavoriteIcon
import com.ashish.movieguide.utils.extensions.setTitleAndYear
import com.ashish.movieguide.utils.extensions.startFavoriteAnimation
import icepick.State
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class MovieDetailActivity : FullDetailContentActivity<MovieDetail, MovieDetailView, MovieDetailPresenter>(),
        MovieDetailView {

    companion object {
        private const val EXTRA_MOVIE = "movie"

        fun createIntent(context: Context, movie: Movie?): Intent {
            return Intent(context, MovieDetailActivity::class.java)
                    .putExtra(EXTRA_MOVIE, movie)
        }
    }

    @Inject lateinit var preferenceHelper: PreferenceHelper

    @JvmField @State var movie: Movie? = null

    private val similarMoviesViewStub: ViewStub by bindView(R.id.similar_content_view_stub)

    private var similarMoviesAdapter: RecyclerViewAdapter<Movie>? = null

    private val onSimilarMovieItemClickLitener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            val movie = similarMoviesAdapter?.getItem<Movie>(position)
            val intent = createIntent(this@MovieDetailActivity, movie)
            startNewActivityWithTransition(view, R.string.transition_movie_poster, intent)
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

        // Show favorite menu item only if user is logged in
        if (preferenceHelper.getId() > 0) {
            menu?.setFavoriteIcon(detailContent.movieRatings?.favorite)
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
        else -> super.onOptionsItemSelected(item)
    }

    override fun changeFavoriteIcon(isFavorite: Boolean) {
        val view = toolbar?.findViewById(R.id.action_favorite)
        if (view is ActionMenuItemView) {
            view.startFavoriteAnimation(isFavorite)
        }
    }

    override fun performCleanup() {
        similarMoviesAdapter?.removeListener()
        super.performCleanup()
    }
}