package com.ashish.movies.ui.base.detail

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import butterknife.bindOptionalView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Credit
import com.ashish.movies.data.models.OMDbDetail
import com.ashish.movies.data.models.Person
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movies.ui.people.detail.PersonDetailActivity
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.Constants.NOT_AVAILABLE
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.show

/**
 * Created by Ashish on Jan 11.
 */
abstract class FullDetailContentActivity<I, V : BaseDetailMvpView<I>, P : BaseDetailPresenter<I, V>>
    : BaseDetailActivity<I, V, P>() {

    private val tmdbRatingView: View? by bindOptionalView(R.id.tmdb_rating_view)
    private val tmdbRatingText: FontTextView? by bindOptionalView(R.id.tmdb_rating_text)

    private val imdbRatingView: View by bindView(R.id.imdb_rating_view)
    private val imdbRatingText: FontTextView by bindView(R.id.imdb_rating_text)

    private val tomatoRatingView: View by bindView(R.id.tomato_rating_view)
    private val tomatoRatingImage: ImageView by bindView(R.id.tomato_rating_image)
    private val tomatoRatingText: FontTextView by bindView(R.id.tomato_rating_text)

    private val flixterScoreView: View by bindView(R.id.flixter_score_view)
    private val flixterScoreImage: ImageView by bindView(R.id.flixter_score_image)
    private val flixterScoreText: FontTextView by bindView(R.id.flixter_score_text)

    private val metascoreView: View by bindView(R.id.metascore_view)
    private val metascoreText: FontTextView by bindView(R.id.metascore_text)

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
        startActivityWithTransition(view, R.string.transition_person_profile, intent)
    }

    override fun showOMDbDetail(omDbDetail: OMDbDetail) {
        with(omDbDetail) {
            setMetaScore(omDbDetail.Metascore)
            setIMDbRating(omDbDetail.imdbRating)
            setFlixterScore(omDbDetail.tomatoUserRating)
            setTomatoRating(omDbDetail.tomatoRating, omDbDetail.tomatoImage)
        }
    }

    protected fun setTMDbRating(voteAverage: Double?) {
        val tmdbRating = voteAverage.toString()
        if (tmdbRating.isNotNullOrEmpty() && tmdbRating != "0") {
            tmdbRatingView?.show()
            tmdbRatingText?.text = tmdbRating
        }
    }

    private fun setIMDbRating(imdbRating: String?) {
        if (imdbRating.isNotNullOrEmpty() && imdbRating != NOT_AVAILABLE) {
            imdbRatingView.show()
            imdbRatingText.text = imdbRating
        }
    }

    private fun setTomatoRating(tomatoRating: String?, tomatoImage: String?) {
        if (tomatoRating.isNotNullOrEmpty() && tomatoRating != NOT_AVAILABLE) {
            tomatoRatingView.show()
            tomatoRatingText.text = tomatoRating

            if (tomatoImage == "certified") {
                tomatoRatingImage.setImageResource(R.drawable.ic_rt_certified)
            } else if (tomatoImage == "fresh") {
                tomatoRatingImage.setImageResource(R.drawable.ic_rt_fresh)
            } else if (tomatoImage == "rotten") {
                tomatoRatingImage.setImageResource(R.drawable.ic_rt_rotten)
            }
        }
    }

    private fun setFlixterScore(tomatoUserRating: String?) {
        if (tomatoUserRating.isNotNullOrEmpty() && tomatoUserRating != NOT_AVAILABLE) {
            flixterScoreView.show()
            flixterScoreText.text = tomatoUserRating

            val flixterScore = tomatoUserRating?.toFloat() ?: 0f
            if (flixterScore > 3.4) {
                flixterScoreImage.setImageResource(R.drawable.ic_flixter_good)
            } else {
                flixterScoreImage.setImageResource(R.drawable.ic_flixter_bad)
            }
        }
    }

    private fun setMetaScore(metaScore: String?) {
        if (metaScore.isNotNullOrEmpty() && metaScore != NOT_AVAILABLE) {
            metascoreView.show()
            metascoreText.text = metaScore

            val metaScoreInt = metaScore?.toInt() ?: 0
            if (metaScoreInt > 60) {
                metascoreText.setBackgroundColor(Color.parseColor("#66CC33"))
            } else if (metaScoreInt > 40 && metaScoreInt < 61) {
                metascoreText.setBackgroundColor(Color.parseColor("#FFCC33"))
            } else {
                metascoreText.setBackgroundColor(Color.parseColor("#FF0000"))
            }
        }
    }

    override fun getCastItemClickListener() = onCastItemClickListener

    override fun getCrewItemClickListener() = onCrewItemClickListener
}