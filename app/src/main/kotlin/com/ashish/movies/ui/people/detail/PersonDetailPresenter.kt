package com.ashish.movies.ui.people.detail

import com.ashish.movies.R
import com.ashish.movies.data.interactors.PeopleInteractor
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.PersonDetail
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import com.ashish.movies.ui.base.detail.BaseDetailView
import com.ashish.movies.utils.extensions.convertListToCommaSeparatedText
import com.ashish.movies.utils.extensions.getFormattedMediumDate
import javax.inject.Inject

/**
 * Created by Ashish on Jan 04.
 */
class PersonDetailPresenter @Inject constructor(val peopleInteractor: PeopleInteractor)
    : BaseDetailPresenter<PersonDetail, BaseDetailView<PersonDetail>>() {

    override fun getDetailContent(id: Long) = peopleInteractor.getFullPeopleDetail(id)

    override fun addContents(fullDetailContent: FullDetailContent<PersonDetail>) {
        fullDetailContent.detailContent?.apply {
            contentList.add(biography ?: "")
            contentList.add(birthday.getFormattedMediumDate())
            contentList.add(placeOfBirth ?: "")
            contentList.add(deathday.getFormattedMediumDate())
            contentList.add(fullDetailContent.omdbDetail?.Awards ?: "")
            contentList.add(alsoKnownAs.convertListToCommaSeparatedText { it })
        }
    }

    override fun getCredits(detailContent: PersonDetail?) = detailContent?.combinedCredits

    override fun getErrorMessageId() = R.string.error_load_person_detail
}