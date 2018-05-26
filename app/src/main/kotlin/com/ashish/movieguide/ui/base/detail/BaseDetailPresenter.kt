package com.ashish.movieguide.ui.base.detail

import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.common.FullDetailContent
import com.ashish.movieguide.data.network.entities.tmdb.CreditResults
import com.ashish.movieguide.data.network.entities.tmdb.ImageItem
import com.ashish.movieguide.ui.base.mvp.RxPresenter
import com.ashish.movieguide.utils.AuthException
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
            loadFreshData(id)
        }
    }

    private fun loadFreshData(id: Long?) {
        if (id != null) {
            view?.showProgress()
            addDisposable(getDetailContent(id)
                    .doOnNext { fullDetailContent = it }
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ showDetailContent(it) }, { onLoadDetailError(it, getErrorMessageId()) }))
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
                showCredits(getCredits(detailContent))
            }

            fullDetailContent.omdbDetail?.let { showOMDbDetail(it) }
            hideProgress()
        }
    }

    private fun showAllImages(detailContent: I) {
        val imageUrlList = ArrayList<String>()
        addImages(imageUrlList, getPosterImages(detailContent)) { it.getPosterUrl() }
        addImages(imageUrlList, getBackdropImages(detailContent)) { it.getBackdropUrl() }

        if (imageUrlList.isNotEmpty()) {
            view?.showImageList(imageUrlList)
        }
    }

    private fun addImages(urlList: ArrayList<String>, imageItemList: List<ImageItem>?,
                          getImageUrl: (String?) -> String?) {
        if (imageItemList.isNotNullOrEmpty()) {
            val posterImageUrlList = imageItemList!!
                    .mapNotNull { getImageUrl(it.filePath) }
                    .toList()
            urlList.addAll(posterImageUrlList)
        }
    }

    abstract fun getContentList(fullDetailContent: FullDetailContent<I, T>): List<String>

    abstract fun getBackdropImages(detailContent: I): List<ImageItem>?

    abstract fun getPosterImages(detailContent: I): List<ImageItem>?

    abstract fun getCredits(detailContent: I): CreditResults?

    private fun showCredits(creditResults: CreditResults?) {
        view?.run {
            showItemList(creditResults?.cast) { showCastList(it) }
            showItemList(creditResults?.crew) { showCrewList(it) }
        }
    }

    private fun onLoadDetailError(t: Throwable, messageId: Int) {
        Timber.e(t)
        view?.run {
            showErrorToast(t, messageId)
            finishActivity()
        }
    }

    abstract fun getErrorMessageId(): Int

    private fun showErrorToast(t: Throwable, messageId: Int) {
        view?.run {
            when (t) {
                is IOException -> showToastMessage(R.string.error_no_internet)
                is AuthException -> showToastMessage(R.string.error_not_logged_in)
                else -> showToastMessage(messageId)
            }
        }
    }

    protected fun <T> showItemList(itemList: List<T>?, showData: (List<T>) -> Unit) {
        if (itemList != null && itemList.isNotEmpty()) showData(itemList)
    }
}