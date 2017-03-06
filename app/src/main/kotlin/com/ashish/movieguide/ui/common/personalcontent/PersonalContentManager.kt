package com.ashish.movieguide.ui.common.personalcontent

import com.ashish.movieguide.R
import com.ashish.movieguide.data.interactors.AuthInteractor
import com.ashish.movieguide.data.models.AccountState
import com.ashish.movieguide.data.models.Favorite
import com.ashish.movieguide.data.models.Watchlist
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.utils.AuthException
import com.ashish.movieguide.utils.Logger
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * This class will handle marking movie, tv show as favorite
 * and adding/removing movie, tv show to/from watchlist.
 */
@ActivityScope
class PersonalContentManager @Inject constructor(
        private val authInteractor: AuthInteractor,
        private val preferenceHelper: PreferenceHelper,
        private val schedulerProvider: BaseSchedulerProvider,
        private val contentStatusObserver: PersonalContentStatusObserver
) {

    private var isFavorite = false
    private var isInWatchlist = false
    private var view: PersonalContentView? = null

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun setView(view: PersonalContentView?) {
        this.view = view
    }

    fun setAccountState(accountState: AccountState?) {
        // Show favorite and watchlist menu item only if user is logged in
        if (preferenceHelper.getId() > 0) {
            isFavorite = accountState?.favorite ?: false
            view?.setFavoriteIcon(isFavorite)

            isInWatchlist = accountState?.watchlist ?: false
            view?.changeWatchlistMenuItem(isInWatchlist)
        }
    }

    fun markAsFavorite(mediaId: Long?, mediaType: String) {
        if (mediaId != null) {
            isFavorite = !isFavorite
            view?.animateFavoriteIcon(isFavorite)

            val favorite = Favorite(isFavorite, mediaId, mediaType)
            compositeDisposable.add(authInteractor.markAsFavorite(favorite)
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ onMarkAsFavoriteSuccesss(mediaId) }, { onMarkAsFavoriteError(it) }))
        }
    }

    private fun onMarkAsFavoriteSuccesss(mediaId: Long) {
        // Notify subscriber if the content is removed from favorites
        if (!isFavorite) {
            contentStatusObserver.notifyContentRemoved(mediaId)
        }
    }

    private fun onMarkAsFavoriteError(t: Throwable) {
        Logger.e(t)
        view?.apply {
            if (t is AuthException) {
                showMessage(R.string.error_not_logged_in)
            } else {
                showMessage(R.string.error_mark_favorite)
            }
        }

        isFavorite = !isFavorite
        view?.setFavoriteIcon(isFavorite)
    }

    fun addToWatchlist(mediaId: Long?, mediaType: String) {
        if (mediaId != null) {
            isInWatchlist = !isInWatchlist
            view?.changeWatchlistMenuItem(isInWatchlist)

            val watchlist = Watchlist(isInWatchlist, mediaId, mediaType)
            compositeDisposable.add(authInteractor.addToWatchlist(watchlist)
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ onAddToWatchlistSuccess(mediaId) }, { onAddToWatchlistError(it) }))
        }
    }

    private fun onAddToWatchlistSuccess(mediaId: Long) {
        showWatchlistMessage(R.string.success_add_to_watchlist, R.string.success_remove_watchlist)

        // Notify subscriber if the content is removed from watchlist
        if (!isInWatchlist) {
            contentStatusObserver.notifyContentRemoved(mediaId)
        }
    }

    private fun onAddToWatchlistError(t: Throwable) {
        Logger.e(t)
        view?.apply {
            if (t is AuthException) {
                showMessage(R.string.error_not_logged_in)
            } else {
                showWatchlistMessage(R.string.error_add_to_watchlist, R.string.error_remove_watchlist)
            }
        }

        isInWatchlist = !isInWatchlist
        view?.changeWatchlistMenuItem(isInWatchlist)
    }

    private fun showWatchlistMessage(addMessage: Int, removeMessage: Int) {
        view?.apply {
            if (isInWatchlist) {
                showMessage(addMessage)
            } else {
                showMessage(removeMessage)
            }
        }
    }

    fun unsubscribe() = compositeDisposable.clear()
}