package com.ashish.movieguide.ui.base.detail.fulldetail

import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewCompat
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.Credit
import com.ashish.movieguide.data.network.entities.tmdb.Person
import com.ashish.movieguide.data.network.entities.tmdb.YouTubeVideo
import com.ashish.movieguide.ui.base.detail.BaseDetailActivity
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movieguide.ui.common.adapter.VideoAdapter
import com.ashish.movieguide.ui.people.detail.PersonDetailActivity
import com.ashish.movieguide.utils.extensions.openUrl
import com.ashish.movieguide.utils.extensions.show
import kotlinx.android.synthetic.main.layout_detail_app_bar.*
import kotlinx.android.synthetic.main.layout_detail_ratings.*
import kotlinx.android.synthetic.main.layout_detail_trailer_fab.*
import kotlinx.android.synthetic.main.layout_detail_videos_stub.*

/**
 * This is base class and extension of [BaseDetailActivity] which
 * handles all the logic for showing different ratings of media content from OMDb api.
 * @param I TMDB Item
 * @param T Trakt Item
 */
abstract class FullDetailContentActivity<I, T, V : FullDetailContentView<I>,
        P : FullDetailContentPresenter<I, T, V>>
    : BaseDetailActivity<I, T, V, P>(), FullDetailContentView<I> {

    private var videoAdapter: VideoAdapter? = null

    private val onVideoItemClickListener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            openUrl(videoAdapter!!.youTubeVideos[position].videoUrl)
        }
    }

    private val onCastItemClickListener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            startPersonDetailActivity(castAdapter, position, view)
        }
    }

    private val onCrewItemClickListener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            startPersonDetailActivity(crewAdapter, position, view)
        }
    }

    private fun startPersonDetailActivity(adapter: RecyclerViewAdapter<Credit>?, position: Int, view: View) {
        val credit = adapter?.getItem<Credit>(position)
        val person = Person(credit?.id, credit?.name, profilePath = credit?.profilePath)
        val intent = PersonDetailActivity.createIntent(this, person)
        startNewActivityWithTransition(view, R.string.transition_person_profile, intent)
    }

    override fun showRatingCard() = ratingCardView.show()

    override fun showImdbRating(imdbRating: String) {
        imdbRatingView.setText(imdbRating)
    }

    override fun showRottenTomatoesRating(tomatoMeter: String, drawableResId: Int) {
        tomatoRatingView.setDrawableResource(drawableResId)
        tomatoRatingView.setText(String.format(getString(R.string.meter_count_format), tomatoMeter))
    }

    override fun showAudienceScore(audienceScore: String, drawableResId: Int) {
        audienceScoreView.setDrawableResource(drawableResId)
        audienceScoreView.setText(String.format(getString(R.string.meter_count_format), audienceScore))
    }

    override fun showMetaScore(metaScore: String, color: Int) {
        metascoreView.show()
        metascoreText.text = metaScore
        metascoreText.setBackgroundColor(color)
    }

    override fun showTmdbRating(tmdbRating: String) {
        tmdbRatingView.setText(tmdbRating)
    }

    override fun showTrailerFAB(trailerUrl: String) {
        playTrailerFAB.run {
            postDelayed({ animate().alpha(1f).scaleX(1f).scaleY(1f).start() }, 80L)
            setOnClickListener { openUrl(trailerUrl) }
        }
    }

    override fun showYouTubeVideos(youTubeVideos: List<YouTubeVideo>) {
        videoAdapter = VideoAdapter(youTubeVideos, onVideoItemClickListener)
        inflateViewStubRecyclerView(videosViewStub, R.id.detailVideosRecyclerView, videoAdapter!!)
    }

    override fun getCastItemClickListener() = onCastItemClickListener

    override fun getCrewItemClickListener() = onCrewItemClickListener

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        super.onOffsetChanged(appBarLayout, verticalOffset)

        // Show or hide FAB depending upon whether appbar is collapsing or expanding
        playTrailerFAB.run {
            val isCollapsing = collapsingToolbar.height + verticalOffset <
                    2.4 * ViewCompat.getMinimumHeight(collapsingToolbar)
            if (isCollapsing) hide() else show()
        }
    }

    override fun finishAfterTransition() {
        // Hide FAB when exiting this Activity for nice FAB animation
        playTrailerFAB.hide()
        super.finishAfterTransition()
    }

    override fun performCleanup() {
        videoAdapter?.removeListener()
        super.performCleanup()
    }
}