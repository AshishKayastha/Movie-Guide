package com.ashish.movies.ui.people.detail

import com.ashish.movies.R
import com.ashish.movies.data.interactors.PeopleInteractor
import com.ashish.movies.data.models.PersonDetail
import com.ashish.movies.ui.base.detail.BaseDetailMvpView
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import javax.inject.Inject

/**
 * Created by Ashish on Jan 04.
 */
class PersonDetailPresenter @Inject constructor(val peopleInteractor: PeopleInteractor)
    : BaseDetailPresenter<PersonDetail, BaseDetailMvpView<PersonDetail>>() {

    override fun getDetailContent(id: Long) = peopleInteractor.getFullPeopleDetail(id)

    override fun getCredits(detailContent: PersonDetail?) = detailContent?.combinedCredits

    override fun getErrorMessageId() = R.string.error_load_person_detail
}