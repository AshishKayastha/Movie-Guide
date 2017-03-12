package com.ashish.movieguide.ui.people.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.tmdb.Credit
import com.ashish.movieguide.data.models.tmdb.Movie
import com.ashish.movieguide.data.models.tmdb.Person
import com.ashish.movieguide.data.models.tmdb.PersonDetail
import com.ashish.movieguide.data.models.tmdb.ProfileImages
import com.ashish.movieguide.data.models.tmdb.TVShow
import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movieguide.ui.base.detail.BaseDetailActivity
import com.ashish.movieguide.ui.base.detail.BaseDetailView
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movieguide.ui.movie.detail.MovieDetailActivity
import com.ashish.movieguide.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_PERSON
import com.ashish.movieguide.utils.Constants.MEDIA_TYPE_MOVIE
import com.ashish.movieguide.utils.Constants.MEDIA_TYPE_TV
import com.ashish.movieguide.utils.Constants.TMDB_URL
import com.ashish.movieguide.utils.extensions.getOriginalImageUrl
import com.ashish.movieguide.utils.extensions.getProfileUrl
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import icepick.State

/**
 * Created by Ashish on Jan 04.
 */
class PersonDetailActivity : BaseDetailActivity<PersonDetail,
        BaseDetailView<PersonDetail>, PersonDetailPresenter>() {

    companion object {
        private const val EXTRA_PERSON = "person"

        @JvmStatic
        fun createIntent(context: Context, person: Person?): Intent {
            return Intent(context, PersonDetailActivity::class.java)
                    .putExtra(EXTRA_PERSON, person)
        }
    }

    @JvmField @State var person: Person? = null

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
        val mediaType = credit?.mediaType

        if (MEDIA_TYPE_MOVIE == mediaType) {
            val movie = Movie(credit.id, credit.title, posterPath = credit.posterPath)
            val intent = MovieDetailActivity.createIntent(this@PersonDetailActivity, movie)
            startNewActivityWithTransition(view, R.string.transition_movie_poster, intent)

        } else if (MEDIA_TYPE_TV == mediaType) {
            val tvShow = TVShow(credit.id, credit.name, posterPath = credit.posterPath)
            val intent = TVShowDetailActivity.createIntent(this@PersonDetailActivity, tvShow)
            startNewActivityWithTransition(view, R.string.transition_tv_poster, intent)
        }
    }

    override fun injectDependencies(builderHost: ActivityComponentBuilderHost) {
        builderHost.getActivityComponentBuilder(PersonDetailActivity::class.java,
                PersonDetailComponent.Builder::class.java)
                .withModule(ActivityModule(this))
                .build()
                .inject(this)
    }

    override fun getLayoutId() = R.layout.activity_detail_person

    override fun getIntentExtras(extras: Bundle?) {
        person = extras?.getParcelable(EXTRA_PERSON)
    }

    override fun getTransitionNameId() = R.string.transition_person_profile

    override fun loadDetailContent() {
        presenter?.loadDetailContent(person?.id)
    }

    override fun getBackdropPath() = ""

    override fun getPosterPath() = person?.profilePath.getProfileUrl()

    override fun showDetailContent(detailContent: PersonDetail) {
        detailContent.apply {
            titleText.text = name
            this@PersonDetailActivity.imdbId = imdbId
            showProfileBackdropImage(detailContent.images)
        }

        super.showDetailContent(detailContent)
    }

    private fun showProfileBackdropImage(images: ProfileImages?) {
        val profileImages = images?.profiles
        if (profileImages.isNotNullOrEmpty()) {
            val backdropPath = profileImages!![profileImages.size - 1].filePath
            if (getBackdropPath().isNullOrEmpty() && backdropPath.isNotNullOrEmpty()) {
                showBackdropImage(backdropPath.getOriginalImageUrl())
            }
        } else {
            showBackdropImage(getPosterPath())
        }
    }

    override fun getDetailContentType() = ADAPTER_TYPE_PERSON

    override fun getItemTitle() = person?.name ?: ""

    override fun getCastItemClickListener() = onCastItemClickListener

    override fun getCrewItemClickListener() = onCrewItemClickListener

    override fun getShareText(): CharSequence {
        return person!!.name + "\n\n" + TMDB_URL + "person/" + person!!.id
    }
}