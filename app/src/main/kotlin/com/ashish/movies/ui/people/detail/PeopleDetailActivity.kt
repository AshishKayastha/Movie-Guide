package com.ashish.movies.ui.people.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.People
import com.ashish.movies.data.models.PeopleDetail
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.detail.BaseDetailActivity
import com.ashish.movies.ui.base.detail.BaseDetailMvpView
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
}