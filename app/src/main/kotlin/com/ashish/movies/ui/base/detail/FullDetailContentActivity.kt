package com.ashish.movies.ui.base.detail

import android.graphics.Color
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Credit
import com.ashish.movies.data.models.OMDbDetail
import com.ashish.movies.data.models.Person
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movies.ui.people.detail.PersonDetailActivity
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.show

/**
 * Created by Ashish on Jan 11.
 */
abstract class FullDetailContentActivity<I, V : BaseDetailView<I>, P : BaseDetailPresenter<I, V>>
    : BaseDetailActivity<I, V, P>() {

    private val ratingCardView: CardView by bindView(R.id.rating_card_view)
    private val tmdbRatingView: View by bindView(R.id.tmdb_rating_view)
    private val tmdbVotesText: FontTextView by bindView(R.id.tmdb_votes_text)
    private val tmdbRatingText: FontTextView by bindView(R.id.tmdb_rating_text)

    private val imdbRatingView: View by bindView(R.id.imdb_rating_view)
    private val imdbVotesText: FontTextView by bindView(R.id.imdb_votes_text)
    private val imdbRatingText: FontTextView by bindView(R.id.imdb_rating_text)

    private val tomatoRatingView: View by bindView(R.id.tomato_rating_view)
    private val tomatoRatingImage: ImageView by bindView(R.id.tomato_rating_image)
    private val tomatoRatingText: FontTextView by bindView(R.id.tomato_rating_text)

    private val audienceScoreView: View by bindView(R.id.audience_score_view)
    private val audienceScoreImage: ImageView by bindView(R.id.audience_score_image)
    private val audienceScoreText: FontTextView by bindView(R.id.audience_score_text)

    private val metascoreView: View by bindView(R.id.metascore_view)
    private val metascoreText: FontTextView by bindView(R.id.metascore_text)
    private val metascoreTextTwo: FontTextView by bindView(R.id.metascore_text_2)

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

    protected fun startPersonDetailActivity(adapter: RecyclerViewAdapter<Credit>?, position: Int, view: View) {
        val credit = adapter?.getItem<Credit>(position)
        val person = Person(credit?.id, credit?.name, profilePath = credit?.profilePath)
        val intent = PersonDetailActivity.createIntent(this, person)
        startNewActivityWithTransition(view, R.string.transition_person_profile, intent)
    }

    override fun showOMDbDetail(omDbDetail: OMDbDetail) {
        super.showOMDbDetail(omDbDetail)
        with(omDbDetail) {
            if (isValidRating(imdbRating) || isValidRating(tomatoRating) || isValidRating(tomatoUserRating)
                    || isValidRating(Metascore)) {
                ratingCardView.show()
                setMetaScore(Metascore)
                setIMDbRating(imdbRating, imdbVotes)
                setAudienceScore(tomatoUserRating)
                setTomatoRating(tomatoMeter, tomatoImage)
            }
        }
    }

    protected fun setTMDbRating(voteAverage: Double?, voteCount: Int?) {
        val tmdbRating = voteAverage.toString()
        val tmdbVotes = voteCount.toString()
        if (isValidRating(tmdbRating)) {
            ratingCardView.show()
            tmdbRatingView.show()
            tmdbRatingText.text = tmdbRating
            tmdbVotesText.text = String.format(getString(R.string.votes_count_format), tmdbVotes)
        }
    }

    private fun setIMDbRating(imdbRating: String?, imdbVotes: String?) {
        if (isValidRating(imdbRating)) {
            imdbRatingView.show()
            imdbRatingText.text = imdbRating
            imdbVotesText.text = String.format(getString(R.string.votes_count_format), imdbVotes)
        }
    }

    private fun setTomatoRating(tomatoRating: String?, tomatoImage: String?) {
        if (isValidRating(tomatoRating)) {
            tomatoRatingView.show()
            tomatoRatingText.text = String.format(getString(R.string.meter_count_format), tomatoRating)

            if (tomatoImage == "certified") {
                tomatoRatingImage.setImageResource(R.drawable.ic_rt_certified)
            } else if (tomatoImage == "fresh") {
                tomatoRatingImage.setImageResource(R.drawable.ic_rt_fresh)
            } else if (tomatoImage == "rotten") {
                tomatoRatingImage.setImageResource(R.drawable.ic_rt_rotten)
            }
        }
    }

    private fun setAudienceScore(tomatoUserRating: String?) {
        if (isValidRating(tomatoUserRating)) {
            audienceScoreView.show()
            audienceScoreText.text = tomatoUserRating

            val flixterScore = tomatoUserRating!!.toFloat()
            if (flixterScore >= 3.5) {
                audienceScoreImage.setImageResource(R.drawable.ic_audience_score_good)
            } else {
                audienceScoreImage.setImageResource(R.drawable.ic_audience_score_bad)
            }
        }
    }

    private fun setMetaScore(metaScore: String?) {
        if (isValidRating(metaScore)) {
            metascoreView.show()
            metascoreText.text = metaScore
            metascoreTextTwo.text = metaScore

            val metaScoreInt = metaScore!!.toInt()
            if (metaScoreInt > 60) {
                metascoreText.setBackgroundColor(Color.parseColor("#66CC33"))
            } else if (metaScoreInt > 40 && metaScoreInt < 61) {
                metascoreText.setBackgroundColor(Color.parseColor("#FFCC33"))
            } else {
                metascoreText.setBackgroundColor(Color.parseColor("#FF0000"))
            }
        }
    }

    private fun isValidRating(rating: String?): Boolean {
        return rating.isNotNullOrEmpty() && rating != "N/A" && rating != "0"
    }

    override fun getCastItemClickListener() = onCastItemClickListener

    override fun getCrewItemClickListener() = onCrewItemClickListener
}