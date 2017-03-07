package com.ashish.movieguide.ui.common.rating

import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.Status
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.utils.Logger
import com.ashish.movieguide.utils.Utils
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@ActivityScope
class RatingManager @Inject constructor(private val schedulerProvider: BaseSchedulerProvider) {

    private var view: ContentRatingView? = null
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun setView(view: ContentRatingView?) {
        this.view = view
    }

    fun saveRating(saveRatingObservable: Observable<Status>, rating: Double) {
        performActionIfOnline {
            compositeDisposable.add(saveRatingObservable
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ view?.showSavedRating(rating) }, { onSaveRatingFailed(it) }))
        }
    }

    private fun onSaveRatingFailed(t: Throwable) {
        Logger.e(t)
        view?.showMessage(R.string.error_save_rating)
    }

    fun deleteRating(deleteRatingObservable: Observable<Status>) {
        performActionIfOnline {
            compositeDisposable.add(deleteRatingObservable
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ view?.showSavedRating(0.0) }, { onDeleteRatingFailed(it) }))
        }
    }

    private fun performActionIfOnline(onlineAction: () -> Unit) {
        if (Utils.isOnline()) {
            onlineAction.invoke()
        } else {
            view?.showMessage(R.string.error_no_internet)
        }
    }

    private fun onDeleteRatingFailed(t: Throwable) {
        Logger.e(t)
        view?.showMessage(R.string.error_delete_rating)
    }

    fun unsubscribe() = compositeDisposable.clear()
}