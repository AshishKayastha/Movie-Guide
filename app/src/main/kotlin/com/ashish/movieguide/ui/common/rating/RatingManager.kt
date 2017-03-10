package com.ashish.movieguide.ui.common.rating

import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.Status
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.utils.Logger
import com.ashish.movieguide.utils.Utils
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@ActivityScope
class RatingManager @Inject constructor(
        private val schedulerProvider: BaseSchedulerProvider,
        private val ratingChangeObserver: RatingChangeObserver
) {

    private var view: ContentRatingView? = null
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun setView(view: ContentRatingView?) {
        this.view = view
    }

    fun saveRating(saveRatingObservable: Single<Status>, mediaId: Long, rating: Double) {
        performActionIfOnline {
            compositeDisposable.add(saveRatingObservable
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ onSaveRatingSuccess(mediaId, rating) }, { onSaveRatingFailed(it) }))
        }
    }

    private fun onSaveRatingSuccess(mediaId: Long, rating: Double) {
        view?.apply {
            showSavedRating(rating)
            showToastMessage(R.string.success_save_rating)
        }

        ratingChangeObserver.notifyRatingChanged(mediaId, rating)
    }

    private fun onSaveRatingFailed(t: Throwable) {
        Logger.e(t)
        view?.showMessage(R.string.error_save_rating)
    }

    fun deleteRating(deleteRatingObservable: Single<Status>, mediaId: Long) {
        performActionIfOnline {
            compositeDisposable.add(deleteRatingObservable
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ onDeleteRatingSuccess(mediaId) }, { onDeleteRatingFailed(it) }))
        }
    }

    private fun onDeleteRatingSuccess(mediaId: Long) {
        view?.apply {
            showSavedRating(0.0)
            showToastMessage(R.string.success_delete_rating)
        }

        ratingChangeObserver.notifyRatingChanged(mediaId, 0.0)
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