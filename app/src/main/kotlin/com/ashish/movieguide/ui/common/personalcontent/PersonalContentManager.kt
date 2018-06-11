package com.ashish.movieguide.ui.common.personalcontent

import com.ashish.movieguide.R
import com.ashish.movieguide.data.interactors.AuthInteractor
import com.ashish.movieguide.data.network.entities.tmdb.Favorite
import com.ashish.movieguide.data.network.entities.tmdb.Watchlist
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.utils.AuthException
import com.ashish.movieguide.utils.Utils
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

/**
 * This class will handle marking movie, tv show as favorite
 * and adding/removing movie, tv show to/from watchlist.
 */
@ActivityScope
class PersonalContentManager @Inject constructor(
        private val authInteractor: AuthInteractor,
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

    fun markAsFavorite(mediaId: Long?, mediaType: String) {
        performActionIfOnline {
            if (mediaId != null) {
                isFavorite = !isFavorite
                view?.animateFavoriteIcon(isFavorite)

                val favorite = Favorite(isFavorite, mediaId, mediaType)
                compositeDisposable.add(authInteractor.markAsFavorite(favorite)
                        .observeOn(schedulerProvider.ui())
                        .subscribe({ onMarkAsFavoriteSuccesss(mediaId, mediaType) }, ::onMarkAsFavoriteError))
            }
        }
    }

    private fun onMarkAsFavoriteSuccesss(mediaId: Long, mediaType: String) {
        // Notify subscriber if the content is removed from favorites
        if (!isFavorite) {
            contentStatusObserver.notifyContentRemoved(mediaId, mediaType)
        }
    }

    private fun onMarkAsFavoriteError(t: Throwable) {
        Timber.e(t)
        isFavorite = !isFavorite
        view?.run {
            setFavoriteIcon(isFavorite)
            if (t is AuthException) {
                showMessage(R.string.error_not_logged_in)
            } else {
                showMessage(R.string.error_mark_favorite)
            }
        }
    }

    fun addToWatchlist(mediaId: Long?, mediaType: String) {
        performActionIfOnline {
            if (mediaId != null) {
                isInWatchlist = !isInWatchlist
                view?.changeWatchlistMenuItem(isInWatchlist)

                val watchlist = Watchlist(isInWatchlist, mediaId, mediaType)
                compositeDisposable.add(authInteractor.addToWatchlist(watchlist)
                        .observeOn(schedulerProvider.ui())
                        .subscribe({ onAddToWatchlistSuccess(mediaId, mediaType) }, ::onAddToWatchlistError))
            }
        }
    }

    private fun onAddToWatchlistSuccess(mediaId: Long, mediaType: String) {
        showWatchlistMessage(R.string.success_add_to_watchlist, R.string.success_remove_watchlist)

        // Notify subscriber if the content is removed from watchlist
        if (!isInWatchlist) {
            contentStatusObserver.notifyContentRemoved(mediaId, mediaType)
        }
    }

    private fun onAddToWatchlistError(t: Throwable) {
        Timber.e(t)
        isInWatchlist = !isInWatchlist
        view?.run {
            changeWatchlistMenuItem(isInWatchlist)
            if (t is AuthException) {
                showMessage(R.string.error_not_logged_in)
            } else {
                showWatchlistMessage(R.string.error_add_to_watchlist, R.string.error_remove_watchlist)
            }
        }
    }

    private fun showWatchlistMessage(addMessage: Int, removeMessage: Int) {
        view?.run {
            if (isInWatchlist) {
                showMessage(addMessage)
            } else {
                showMessage(removeMessage)
            }
        }
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