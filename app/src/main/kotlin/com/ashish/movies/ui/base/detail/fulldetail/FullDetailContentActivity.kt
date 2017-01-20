package com.ashish.movies.ui.base.detail.fulldetail

import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Credit
import com.ashish.movies.data.models.Person
import com.ashish.movies.ui.base.detail.BaseDetailActivity
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movies.ui.people.detail.PersonDetailActivity
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.extensions.show

/**
 * Created by Ashish on Jan 11.
 */
abstract class FullDetailContentActivity<I, V : FullDetailContentView<I>, P : FullDetailContentPresenter<I, V>>
    : BaseDetailActivity<I, V, P>(), FullDetailContentView<I> {

    private val ratingCardView: CardView by bindView(R.id.rating_card_view)
    private val tmdbRatingView: View by bindView(R.id.tmdb_rating_view)
    private val tmdbRatingText: FontTextView by bindView(R.id.tmdb_rating_text)

    private val imdbRatingView: View by bindView(R.id.imdb_rating_view)
    private val imdbRatingText: FontTextView by bindView(R.id.imdb_rating_text)

    private val tomatoRatingView: View by bindView(R.id.tomato_rating_view)
    private val tomatoRatingImage: ImageView by bindView(R.id.tomato_rating_image)
    private val tomatoRatingText: FontTextView by bindView(R.id.tomato_rating_text)

    private val audienceScoreView: View by bindView(R.id.audience_score_view)
    private val audienceScoreImage: ImageView by bindView(R.id.audience_score_image)
    private val audienceScoreText: FontTextView by bindView(R.id.audience_score_text)

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
        startNewActivityWithTransition(view, R.string.transition_person_profile, intent)
    }

    override fun showRatingCard() = ratingCardView.show()

    override fun showImdbRating(imdbRating: String) {
        imdbRatingView.show()
        imdbRatingText.text = imdbRating
    }

    override fun showRottenTomatoesRating(tomatoMeter: String, drawableResId: Int) {
        tomatoRatingView.show()
        tomatoRatingImage.setImageResource(drawableResId)
        tomatoRatingText.text = String.format(getString(R.string.meter_count_format), tomatoMeter)
    }

    override fun showAudienceScore(audienceScore: String, drawableResId: Int) {
        audienceScoreView.show()
        audienceScoreImage.setImageResource(drawableResId)
        audienceScoreText.text = String.format(getString(R.string.meter_count_format), audienceScore)
    }

    override fun showMetaScore(metaScore: String, color: Int) {
        metascoreView.show()
        metascoreText.text = metaScore
        metascoreText.setBackgroundColor(color)
    }

    override fun showTmdbRating(tmdbRating: String) {
        tmdbRatingView.show()
        tmdbRatingText.text = tmdbRating
    }

    override fun getCastItemClickListener() = onCastItemClickListener

    override fun getCrewItemClickListener() = onCrewItemClickListener
}