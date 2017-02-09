package com.ashish.movies.ui.people.detail

import com.ashish.movies.R
import com.ashish.movies.data.interactors.PeopleInteractor
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.PersonDetail
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import com.ashish.movies.ui.base.detail.BaseDetailView
import com.ashish.movies.utils.extensions.convertListToCommaSeparatedText
import com.ashish.movies.utils.extensions.getFormattedMediumDate
import java.util.*
import javax.inject.Inject

/**
 * Created by Ashish on Jan 04.
 */
class PersonDetailPresenter @Inject constructor(private val peopleInteractor: PeopleInteractor)
    : BaseDetailPresenter<PersonDetail, BaseDetailView<PersonDetail>>() {

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

    override fun getVideos(detailContent: PersonDetail) = null

    override fun getCredits(detailContent: PersonDetail) = detailContent.combinedCredits

    override fun getErrorMessageId() = R.string.error_load_person_detail
}