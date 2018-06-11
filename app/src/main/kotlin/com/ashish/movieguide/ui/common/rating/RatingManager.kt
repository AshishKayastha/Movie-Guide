package com.ashish.movieguide.ui.common.rating

import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.api.trakt.SyncApi
import com.ashish.movieguide.data.network.entities.trakt.SyncItems
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.ui.widget.CustomToast
import com.ashish.movieguide.utils.Utils
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@ActivityScope
class RatingManager @Inject constructor(
        private val syncApi: SyncApi,
        private val schedulerProvider: BaseSchedulerProvider,
        private val ratingChangeObserver: RatingChangeObserver
) {

    private var view: RatingMvpView? = null
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun setView(view: RatingMvpView?) {
        this.view = view
    }

    fun addRating(syncItems: SyncItems, mediaId: Long, rating: Int) {
        performActionIfOnline {
            compositeDisposable.add(syncApi.addRatings(syncItems)
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ onAddRatingSuccess(mediaId, rating) }, ::onAddRatingFailed))
        }
    }

    private fun onAddRatingSuccess(mediaId: Long, rating: Int) {
        view?.run {
            showSavedRating(rating)
            showToastMessage(R.string.success_save_rating, CustomToast.TOAST_TYPE_SUCCESS)
        }

        ratingChangeObserver.notifyRatingChanged(mediaId, rating)
    }

    private fun onAddRatingFailed(t: Throwable) {
        Timber.e(t)
        view?.showMessage(R.string.error_save_rating)
    }

    fun removeRating(syncItems: SyncItems, mediaId: Long) {
        performActionIfOnline {
            compositeDisposable.add(syncApi.removeRatings(syncItems)
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ onRemoveRatingSuccess(mediaId) }, ::onRemoveRatingFailed))
        }
    }

    private fun onRemoveRatingSuccess(mediaId: Long) {
        view?.run {
            showSavedRating(0)
            showToastMessage(R.string.success_delete_rating, CustomToast.TOAST_TYPE_SUCCESS)
        }

        ratingChangeObserver.notifyRatingChanged(mediaId, 0)
    }

    private fun onRemoveRatingFailed(t: Throwable) {
        Timber.e(t)
        view?.showMessage(R.string.error_delete_rating)
    }

    private fun performActionIfOnline(onlineAction: () -> Unit) {
        if (Utils.isOnline()) {
            onlineAction.invoke()
        } else {
            view?.showMessage(R.string.error_no_internet)
        }
    }

    fun unsubscribe() = compositeDisposable.clear()
}