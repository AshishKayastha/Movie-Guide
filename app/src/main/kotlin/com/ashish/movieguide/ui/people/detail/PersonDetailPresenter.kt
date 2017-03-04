package com.ashish.movieguide.ui.people.detail

import com.ashish.movieguide.R
import com.ashish.movieguide.data.interactors.PeopleInteractor
import com.ashish.movieguide.data.models.FullDetailContent
import com.ashish.movieguide.data.models.PersonDetail
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.ui.base.detail.BaseDetailPresenter
import com.ashish.movieguide.ui.base.detail.BaseDetailView
import com.ashish.movieguide.utils.extensions.convertListToCommaSeparatedText
import com.ashish.movieguide.utils.extensions.getFormattedMediumDate
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Ashish on Jan 04.
 */
@ActivityScope
class PersonDetailPresenter @Inject constructor(
        private val peopleInteractor: PeopleInteractor,
        schedulerProvider: BaseSchedulerProvider
) : BaseDetailPresenter<PersonDetail, BaseDetailView<PersonDetail>>(schedulerProvider) {

    override fun getDetailContent(id: Long) = peopleInteractor.getFullPersonDetail(id)

    override fun getContentList(fullDetailContent: FullDetailContent<PersonDetail>): List<String> {
        val contentList = ArrayList<String>()
        fullDetailContent.detailContent?.apply {
            contentList.apply {
                add(biography ?: "")
                add(birthday.getFormattedMediumDate())
                add(placeOfBirth ?: "")
                add(deathday.getFormattedMediumDate())
                add(fullDetailContent.omdbDetail?.Awards ?: "")
                add(alsoKnownAs.convertListToCommaSeparatedText { it })
            }
        }

        return contentList
    }

    override fun getBackdropImages(detailContent: PersonDetail) = detailContent.images?.profiles

    override fun getPosterImages(detailContent: PersonDetail) = null

    override fun getCredits(detailContent: PersonDetail) = detailContent.combinedCredits

    override fun getErrorMessageId() = R.string.error_load_person_detail
}