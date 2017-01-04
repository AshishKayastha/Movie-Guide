package com.ashish.movies.ui.tvshow.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.data.models.TVShowDetail
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.detail.BaseDetailActivity
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter.Companion.ADAPTER_TYPE_TV_SHOW
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.Constants.BACKDROP_W780_URL_PREFIX
import com.ashish.movies.utils.Constants.NOT_AVAILABLE
import com.ashish.movies.utils.Constants.POSTER_W500_URL_PREFIX
import com.ashish.movies.utils.extensions.getFormattedGenres
import com.ashish.movies.utils.extensions.getFormattedReleaseDate
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.setTitleAndYear

/**
 * Created by Ashish on Jan 03.
 */
class TVShowDetailActivity : BaseDetailActivity<TVShowDetail, TVShowDetailMvpView, TVShowDetailPresenter>(),
        TVShowDetailMvpView {

    val statusText: FontTextView by bindView(R.id.status_text)
    val genresText: FontTextView by bindView(R.id.genres_text)
    val releaseDateText: FontTextView by bindView(R.id.release_date_text)
    val similarTVShowsViewStub: ViewStub by bindView(R.id.similar_content_view_stub)

    private var tvShow: TVShow? = null
    private lateinit var similarTVShowsAdapter: RecyclerViewAdapter<TVShow>

    private val onSimilarTVShowItemClickLitener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            val tvShow = similarTVShowsAdapter.getItem<TVShow>(position)
            val intent = TVShowDetailActivity.createIntent(this@TVShowDetailActivity, tvShow)
            startActivityWithTransition(R.string.transition_poster_image, view, intent)
        }
    }

    companion object {
        const val EXTRA_TV_SHOW = "tv_show"

        fun createIntent(context: Context, tvShow: TVShow?): Intent {
            return Intent(context, TVShowDetailActivity::class.java)
                    .putExtra(EXTRA_TV_SHOW, tvShow)
        }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(TVShowDetailModule()).inject(this)
    }

    override fun getLayoutId() = R.layout.activity_detail_movie

    override fun getIntentExtras(extras: Bundle?) {
        tvShow = extras?.getParcelable(EXTRA_TV_SHOW)
    }

    override fun loadDetailContent() = presenter.loadDetailContent(tvShow?.id)

    override fun getBackdropPath(): String {
        val backdropPath = tvShow?.backdropPath
        return if (backdropPath.isNotNullOrEmpty()) BACKDROP_W780_URL_PREFIX + backdropPath else ""
    }

    override fun getPosterPath(): String {
        val posterPath = tvShow?.posterPath
        return if (posterPath.isNotNullOrEmpty()) POSTER_W500_URL_PREFIX + posterPath else ""
    }

    override fun showDetailContent(detailContent: TVShowDetail?) {
        detailContent?.apply {
            titleText.setTitleAndYear(name, firstAirDate)
            overviewText.text = overview ?: NOT_AVAILABLE
            genresText.text = genres.getFormattedGenres()
            statusText.text = status ?: NOT_AVAILABLE
            releaseDateText.text = firstAirDate.getFormattedReleaseDate(this@TVShowDetailActivity)
        }
        super.showDetailContent(detailContent)
    }

    override fun getItemTitle(): String = tvShow?.name ?: ""

    override fun showSimilarTVShowList(similarTVShowList: List<TVShow>) {
        similarTVShowsAdapter = RecyclerViewAdapter(R.layout.list_item_content_alt, ADAPTER_TYPE_TV_SHOW,
                onSimilarTVShowItemClickLitener)

        similarTVShowsViewStub.setOnInflateListener { viewStub, view ->
            val textView = view.findViewById(R.id.similar_content_title) as FontTextView
            textView.setText(R.string.similar_tv_shows_title)
        }

        inflateViewStubRecyclerView(similarTVShowsViewStub, R.id.similar_content_recycler_view,
                similarTVShowsAdapter, similarTVShowList)
    }
}