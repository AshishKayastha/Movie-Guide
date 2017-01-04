package com.ashish.movies.ui.people.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Credit
import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.People
import com.ashish.movies.data.models.PeopleDetail
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.detail.BaseDetailActivity
import com.ashish.movies.ui.base.detail.BaseDetailMvpView
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movies.ui.movie.detail.MovieDetailActivity
import com.ashish.movies.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.Constants.PROFILE_ORIGINAL_URL_PREFIX
import com.ashish.movies.utils.extensions.applyText
import com.ashish.movies.utils.extensions.isNotNullOrEmpty

/**
 * Created by Ashish on Jan 04.
 */
class PeopleDetailActivity : BaseDetailActivity<PeopleDetail, BaseDetailMvpView<PeopleDetail>, PeopleDetailPresenter>() {

    private val birthdayText: FontTextView by bindView(R.id.birthday_text)
    private val placeOfBirthText: FontTextView by bindView(R.id.place_of_birth_text)

    private var people: People? = null

    private val onCastItemClickListener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            onPersonCreditItemClicked(castAdapter, position, view)
        }
    }

    private val onCrewItemClickListener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            onPersonCreditItemClicked(crewAdapter, position, view)
        }
    }

    private fun onPersonCreditItemClicked(adapter: RecyclerViewAdapter<Credit>?, position: Int, view: View) {
        val credit = adapter?.getItem<Credit>(position)

        var intent: Intent? = null
        val mediaType = credit?.mediaType

        if ("movie" == mediaType) {
            val movie = Movie(credit?.id, credit?.title, posterPath = credit?.posterPath)
            intent = MovieDetailActivity.createIntent(this@PeopleDetailActivity, movie)
        } else if ("tv" == mediaType) {
            val tvShow = TVShow(credit?.id, credit?.name, posterPath = credit?.posterPath)
            intent = TVShowDetailActivity.createIntent(this@PeopleDetailActivity, tvShow)
        }

        if (intent != null) {
            startActivityWithTransition(R.string.transition_poster_image, view, intent)
        }
    }

    companion object {
        const val EXTRA_PEOPLE = "people"

        fun createIntent(context: Context, people: People?): Intent {
            return Intent(context, PeopleDetailActivity::class.java)
                    .putExtra(EXTRA_PEOPLE, people)
        }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(PeopleDetailModule()).inject(this)
    }

    override fun getLayoutId() = R.layout.activity_detail_people

    override fun getIntentExtras(extras: Bundle?) {
        people = extras?.getParcelable(EXTRA_PEOPLE)
    }

    override fun loadDetailContent() = presenter.loadDetailContent(people?.id)

    override fun getBackdropPath(): String {
        val backdropPath = people?.profilePath
        return if (backdropPath.isNotNullOrEmpty()) PROFILE_ORIGINAL_URL_PREFIX + backdropPath else ""
    }

    override fun getPosterPath() = getBackdropPath()

    override fun showDetailContent(detailContent: PeopleDetail?) {
        detailContent?.apply {
            titleText.text = name
            overviewText.text = detailContent.biography
            birthdayText.applyText(detailContent.birthday)
            placeOfBirthText.applyText(detailContent.placeOfBirth)
        }
        super.showDetailContent(detailContent)
    }

    override fun getItemTitle() = people?.name ?: ""

    override fun getCastItemClickListener() = onCastItemClickListener

    override fun getCrewItemClickListener() = onCrewItemClickListener
}