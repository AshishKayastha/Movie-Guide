package com.ashish.movieguide.ui.base.detail

import com.ashish.movieguide.R
import com.ashish.movieguide.data.remote.entities.common.FullDetailContent
import com.ashish.movieguide.data.remote.entities.tmdb.CreditResults
import com.ashish.movieguide.data.remote.entities.tmdb.ImageItem
import com.ashish.movieguide.ui.base.mvp.RxPresenter
import com.ashish.movieguide.utils.AuthException
import com.ashish.movieguide.utils.extensions.addAllIfNotNull
import com.ashish.movieguide.utils.extensions.getBackdropUrl
import com.ashish.movieguide.utils.extensions.getPosterUrl
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import timber.log.Timber
import java.io.IOException
import java.util.ArrayList

/**
 * Created by Ashish on Jan 03.
 */
abstract class BaseDetailPresenter<I, T, V : BaseDetailView<I>>(
        schedulerProvider: BaseSchedulerProvider
) : RxPresenter<V>(schedulerProvider) {

    protected var fullDetailContent: FullDetailContent<I, T>? = null

    fun loadDetailContent(id: Long?) {
        if (fullDetailContent != null) {
            showDetailContent(fullDetailContent!!)
        } else {
            fetchFreshData(id)
        }
    }

    private fun fetchFreshData(id: Long?) {
        if (id != null) {
            view?.setLoadingIndicator(true)
            addDisposable(getDetailContent(id)
                    .doOnNext { fullDetailContent = it }
                    .observeOn(schedulerProvider.ui())
                    .subscribe(::showDetailContent, ::onLoadDetailError))
        }
    }

    abstract fun getDetailContent(id: Long): Observable<FullDetailContent<I, T>>

    protected open fun showDetailContent(fullDetailContent: FullDetailContent<I, T>) {
        view?.run {
            val contentList = getContentList(fullDetailContent)
            showDetailContentList(contentList)

            val detailContent = fullDetailContent.detailContent
            if (detailContent != null) {
                showDetailContent(detailContent)
                showAllImages(detailContent)
                showCredits(detailContent)
            }

            fullDetailContent.omdbDetail?.let { showOMDbDetail(it) }
            setLoadingIndicator(false)
        }
    }

    abstract fun getContentList(fullDetailContent: FullDetailContent<I, T>): List<String>

    private fun showAllImages(detailContent: I) {
        val imageUrlList = ArrayList<String>()

        val posterUrlList = getImageUrlList(getPosterImages(detailContent)) { it.getPosterUrl() }
        imageUrlList.addAllIfNotNull(posterUrlList)

        val backdropUrlList = getImageUrlList(getBackdropImages(detailContent)) { it.getBackdropUrl() }
        imageUrlList.addAllIfNotNull(backdropUrlList)

        if (imageUrlList.isNotEmpty()) {
            view?.showImageList(imageUrlList)
        }
    }

    abstract fun getBackdropImages(detailContent: I): List<ImageItem>?

    abstract fun getPosterImages(detailContent: I): List<ImageItem>?

    private fun getImageUrlList(imageItemList: List<ImageItem>?, getImageUrl: (String?) -> String?): List<String>? {
        return if (imageItemList.isNotNullOrEmpty()) {
            imageItemList!!.mapNotNull { getImageUrl(it.filePath) }.toList()
        } else null
    }

    abstract fun getCredits(detailContent: I): CreditResults?

    private fun showCredits(detailContent: I) {
        view?.run {
            val creditResults = getCredits(detailContent)
            showItemList(creditResults?.cast) { showCastList(it) }
            showItemList(creditResults?.crew) { showCrewList(it) }
        }
    }

    protected fun <T> showItemList(itemList: List<T>?, showData: (List<T>) -> Unit) {
        if (itemList.isNotNullOrEmpty()) showData(itemList!!)
    }

    private fun onLoadDetailError(t: Throwable) {
        Timber.e(t)
        view?.run {
            showErrorToast(t)
            finishActivity()
        }
    }

    abstract fun getErrorMessageId(): Int

    private fun showErrorToast(t: Throwable) {
        view?.run {
            when (t) {
                is IOException -> showToastMessage(R.string.error_no_internet)
                is AuthException -> showToastMessage(R.string.error_not_logged_in)
                else -> showToastMessage(getErrorMessageId())
            }
        }
    }
}