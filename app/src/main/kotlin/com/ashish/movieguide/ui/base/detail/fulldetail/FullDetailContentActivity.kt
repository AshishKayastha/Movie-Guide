package com.ashish.movieguide.ui.base.detail.fulldetail

import android.support.design.widget.AppBarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.support.v7.widget.CardView
import android.view.MenuItem
import android.view.View
import android.view.ViewStub
import butterknife.bindView
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.Credit
import com.ashish.movieguide.data.models.Person
import com.ashish.movieguide.data.models.YouTubeVideo
import com.ashish.movieguide.ui.base.detail.BaseDetailActivity
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movieguide.ui.common.adapter.VideoAdapter
import com.ashish.movieguide.ui.people.detail.PersonDetailActivity
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.ui.widget.RatingView
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.openUrl
import com.ashish.movieguide.utils.extensions.setVisibility
import com.ashish.movieguide.utils.extensions.show

/**
 * This is base class and extension of [BaseDetailActivity] which
 * handles all the logic for showing different ratings of media content from OMDb api.
 */
abstract class FullDetailContentActivity<I, V : FullDetailContentView<I>, P : FullDetailContentPresenter<I, V>>
    : BaseDetailActivity<I, V, P>(), FullDetailContentView<I> {

    private val ratingCardView: CardView by bindView(R.id.rating_card_view)

    private val tmdbRatingView: RatingView by bindView(R.id.tmdb_rating_view)
    private val imdbRatingView: RatingView by bindView(R.id.imdb_rating_view)
    private val tomatoRatingView: RatingView by bindView(R.id.tomato_rating_view)
    private val audienceScoreView: RatingView by bindView(R.id.audience_score_view)

    private val metascoreView: View by bindView(R.id.metascore_view)
    private val metascoreText: FontTextView by bindView(R.id.metascore_text)

    private val videosViewStub: ViewStub by bindView(R.id.videos_view_stub)
    private val playTrailerFAB: FloatingActionButton by bindView(R.id.play_trailer_fab)

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

    override fun showDetailContent(detailContent: I) {
        super.showDetailContent(detailContent)
        showOrHideMenu(R.id.action_favorite, getMediaType())
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
        playTrailerFAB.apply {
            postDelayed({ animate().alpha(1f).scaleX(1f).scaleY(1f).start() }, 80L)
            setOnClickListener { openUrl(trailerUrl) }
        }
    }

    override fun showYouTubeVideos(youTubeVideos: List<YouTubeVideo>) {
        videoAdapter = VideoAdapter(youTubeVideos, onVideoItemClickListener)
        inflateViewStubRecyclerView(videosViewStub, R.id.detail_videos_recycler_view, videoAdapter!!)
    }

    override fun getCastItemClickListener() = onCastItemClickListener

    override fun getCrewItemClickListener() = onCrewItemClickListener

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        // Show or hide FAB depending upon whether appbar is collapsing or expanding
        playTrailerFAB.apply {
            val isCollapsing = collapsingToolbar.height + verticalOffset <
                    2.4 * ViewCompat.getMinimumHeight(collapsingToolbar)
            setVisibility(!isCollapsing)
        }

        super.onOffsetChanged(appBarLayout, verticalOffset)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> handleFavoriteAction(item)
        else -> super.onOptionsItemSelected(item)
    }

    private fun handleFavoriteAction(item: MenuItem): Boolean {
        return performAction {
            if (getMediaType().isNotNullOrEmpty()) {
                item.setIcon(R.drawable.ic_favorite_white_24dp)
            }
        }
    }

    protected open fun getMediaType(): String? = null

    override fun handleMarkAsFavoriteError(isFavorite: Boolean) {
        val favItem = menu?.findItem(R.id.action_favorite)
        if (isFavorite) {
            favItem?.setIcon(R.drawable.ic_favorite_white_24dp)
        } else {
            favItem?.setIcon(R.drawable.ic_favorite_border_white_24dp)
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