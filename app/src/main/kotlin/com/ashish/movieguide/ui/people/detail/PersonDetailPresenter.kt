package com.ashish.movieguide.ui.people.detail

import com.ashish.movieguide.R
import com.ashish.movieguide.data.interactors.PeopleInteractor
import com.ashish.movieguide.data.network.entities.common.FullDetailContent
import com.ashish.movieguide.data.network.entities.tmdb.CreditResults
import com.ashish.movieguide.data.network.entities.tmdb.ImageItem
import com.ashish.movieguide.data.network.entities.tmdb.PersonDetail
import com.ashish.movieguide.data.network.entities.trakt.TraktPerson
import com.ashish.movieguide.ui.base.detail.BaseDetailPresenter
import com.ashish.movieguide.ui.base.detail.BaseDetailView
import com.ashish.movieguide.utils.extensions.convertListToCommaSeparatedText
import com.ashish.movieguide.utils.extensions.getFormattedMediumDate
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Ashish on Jan 04.
 */
class PersonDetailPresenter @Inject constructor(
        private val peopleInteractor: PeopleInteractor,
        schedulerProvider: BaseSchedulerProvider
) : BaseDetailPresenter<PersonDetail, TraktPerson, BaseDetailView<PersonDetail>>(schedulerProvider) {

    override fun getDetailContent(id: Long): Observable<FullDetailContent<PersonDetail, TraktPerson>> =
            peopleInteractor.getFullPersonDetail(id)

    override fun getContentList(fullDetailContent: FullDetailContent<PersonDetail, TraktPerson>): List<String> {
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

    override fun getBackdropImages(detailContent: PersonDetail): List<ImageItem>? = detailContent.images?.profiles

    override fun getPosterImages(detailContent: PersonDetail): List<ImageItem>? = null

    override fun getCredits(detailContent: PersonDetail): CreditResults? = detailContent.credits

    override fun getErrorMessageId() = R.string.error_load_person_detail
}