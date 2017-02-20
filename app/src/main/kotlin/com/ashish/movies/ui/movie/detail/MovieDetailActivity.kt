package com.ashish.movies.ui.movie.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.MovieDetail
import com.ashish.movies.di.components.UiComponent
import com.ashish.movies.ui.base.detail.fulldetail.FullDetailContentActivity
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movies.utils.ApiConstants.MEDIA_TYPE_MOVIE
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_MOVIE
import com.ashish.movies.utils.extensions.getBackdropUrl
import com.ashish.movies.utils.extensions.getPosterUrl
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.setTitleAndYear
import icepick.State

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

    override fun injectDependencies(uiComponent: UiComponent) = uiComponent.inject(this)

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
        super.showDetailContent(detailContent)
    }

    override fun getDetailContentType() = ADAPTER_TYPE_MOVIE

    override fun showSimilarMoviesList(similarMoviesList: List<Movie>) {
        similarMoviesAdapter = RecyclerViewAdapter(R.layout.list_item_content_alt, ADAPTER_TYPE_MOVIE,
                onSimilarMovieItemClickLitener)

        inflateViewStubRecyclerView(similarMoviesViewStub, R.id.similar_content_recycler_view,
                similarMoviesAdapter!!, similarMoviesList)
    }

    override fun getMediaType() = MEDIA_TYPE_MOVIE

    override fun performCleanup() {
        similarMoviesAdapter?.removeListener()
        super.performCleanup()
    }
}