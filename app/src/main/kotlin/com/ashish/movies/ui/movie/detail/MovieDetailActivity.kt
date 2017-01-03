package com.ashish.movies.ui.movie.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.view.View
import android.view.ViewStub
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Movie
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.detail.BaseDetailActivity
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter.Companion.ADAPTER_TYPE_MOVIE
import com.ashish.movies.utils.Constants.BACKDROP_W780_URL_PREFIX
import com.ashish.movies.utils.Constants.NOT_AVAILABLE
import com.ashish.movies.utils.Constants.POSTER_W500_URL_PREFIX
import com.ashish.movies.utils.extensions.getFormattedGenres
import com.ashish.movies.utils.extensions.getFormattedNumber
import com.ashish.movies.utils.extensions.getFormattedReleaseDate
import com.ashish.movies.utils.extensions.getFormattedRuntime
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.setTitleAndYear

/**
 * Created by Ashish on Dec 31.
 */
class MovieDetailActivity : BaseDetailActivity<Movie, MovieDetailMvpView, MovieDetailPresenter>(), MovieDetailMvpView,
        AppBarLayout.OnOffsetChangedListener {

    val similarMoviesViewStub: ViewStub by bindView(R.id.similar_content_view_stub)

    private lateinit var similarMoviesAdapter: RecyclerViewAdapter<Movie>

    companion object {
        const val EXTRA_MOVIE = "movie"

        fun createIntent(context: Context, movie: Movie?): Intent {
            return Intent(context, MovieDetailActivity::class.java)
                    .putExtra(EXTRA_MOVIE, movie)
        }
    }

    private val onSimilarMovieItemClickLitener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            val movie = similarMoviesAdapter.getItem<Movie>(position)
            val intent = createIntent(this@MovieDetailActivity, movie)
            startActivityWithTransition(R.string.transition_poster_image, view, intent)
        }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(MovieDetailModule()).inject(this)
    }

    override fun getLayoutId() = R.layout.activity_movie_detail

    override fun getIntentExtras(extras: Bundle?) {
        item = extras?.getParcelable(EXTRA_MOVIE)
    }

    override fun loadDetailContent() = presenter.loadDetailContent(item?.id)

    override fun getBackdropPath(): String {
        val backdropPath = item?.backdropPath
        return if (backdropPath.isNotNullOrEmpty()) BACKDROP_W780_URL_PREFIX + backdropPath else ""
    }

    override fun getPosterPath(): String {
        val posterPath = item?.posterPath
        return if (posterPath.isNotNullOrEmpty()) POSTER_W500_URL_PREFIX + posterPath else ""
    }

    override fun getItemTitle(): String = item?.title ?: ""

    override fun showDetailContent(item: Movie?) {
        item?.apply {
            titleText.setTitleAndYear(title, releaseDate)
            overviewText.text = overview ?: NOT_AVAILABLE
            genresText.text = genres.getFormattedGenres()
            statusText.text = status ?: NOT_AVAILABLE
            budgetText.text = budget.getFormattedNumber()
            revenueText.text = revenue.getFormattedNumber()
            runtimeText.text = runtime.getFormattedRuntime()
            releaseDateText.text = releaseDate.getFormattedReleaseDate(this@MovieDetailActivity)
        }
        super.showDetailContent(item)
    }

    override fun showSimilarContentList(similarItemList: List<Movie>) {
        similarMoviesAdapter = RecyclerViewAdapter(R.layout.list_item_content_alt, ADAPTER_TYPE_MOVIE, onSimilarMovieItemClickLitener)
        inflateViewStubRecyclerView(similarMoviesViewStub, R.id.similar_content_recycler_view,
                similarMoviesAdapter, similarItemList)
    }

    override fun performCleanup() {
        similarMoviesAdapter.removeListener()
        super.performCleanup()
    }
}