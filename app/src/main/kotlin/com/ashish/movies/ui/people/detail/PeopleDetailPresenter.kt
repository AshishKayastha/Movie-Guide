package com.ashish.movies.ui.people.detail

import com.ashish.movies.data.interactors.PeopleInteractor
import com.ashish.movies.data.models.PeopleDetail
import com.ashish.movies.ui.base.detail.BaseDetailMvpView
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashish on Jan 04.
 */
class PeopleDetailPresenter @Inject constructor(val peopleInteractor: PeopleInteractor)
    : BaseDetailPresenter<PeopleDetail, BaseDetailMvpView<PeopleDetail>>() {

    override fun getDetailContent(id: Long): Disposable {
        return peopleInteractor.getPeopleDetailWithCombinedCredits(id)
                .subscribe({ showPeopleDetailContents(it) }, { Timber.e(it) })
    }

    private fun showPeopleDetailContents(peopleDetail: PeopleDetail?) {
        getView()?.apply {
            hideProgress()
            showDetailContent(peopleDetail)

            val creditResults = peopleDetail?.combinedCredits
            showItemList(creditResults?.cast) { showCastList(it) }
            showItemList(creditResults?.crew) { showCrewList(it) }
        }
    }
}