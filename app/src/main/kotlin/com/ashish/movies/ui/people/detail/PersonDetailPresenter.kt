package com.ashish.movies.ui.people.detail

import com.ashish.movies.R
import com.ashish.movies.data.interactors.PeopleInteractor
import com.ashish.movies.data.models.PersonDetail
import com.ashish.movies.ui.base.detail.BaseDetailMvpView
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by Ashish on Jan 04.
 */
class PersonDetailPresenter @Inject constructor(val peopleInteractor: PeopleInteractor)
    : BaseDetailPresenter<PersonDetail, BaseDetailMvpView<PersonDetail>>() {

    override fun getDetailContent(id: Long): Disposable {
        return peopleInteractor.getPeopleDetailWithCombinedCredits(id)
                .subscribe({ showPeopleDetailContents(it) }, {
                    onLoadDetailError(it, R.string.error_load_person_detail)
                })
    }

    private fun showPeopleDetailContents(personDetail: PersonDetail?) {
        getView()?.apply {
            hideProgress()
            showDetailContent(personDetail)
            showCredits(personDetail?.combinedCredits)
        }
    }
}