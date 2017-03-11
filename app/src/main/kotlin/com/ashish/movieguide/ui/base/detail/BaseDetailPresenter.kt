package com.ashish.movieguide.ui.base.detail

import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.CreditResults
import com.ashish.movieguide.data.models.FullDetailContent
import com.ashish.movieguide.data.models.ImageItem
import com.ashish.movieguide.ui.base.mvp.RxPresenter
import com.ashish.movieguide.utils.AuthException
import com.ashish.movieguide.utils.extensions.getBackdropUrl
import com.ashish.movieguide.utils.extensions.getPosterUrl
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.logger.Logger
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import java.io.IOException
import java.util.ArrayList

/**
 * Created by Ashish on Jan 03.
 */
abstract class BaseDetailPresenter<I, V : BaseDetailView<I>>(
        schedulerProvider: BaseSchedulerProvider
) : RxPresenter<V>(schedulerProvider) {

    protected var fullDetailContent: FullDetailContent<I>? = null

    fun loadDetailContent(id: Long?) {
        if (fullDetailContent != null) {
            showDetailContent(fullDetailContent!!)
        } else {
            loadFreshData(id)
        }
    }

    private fun loadFreshData(id: Long?) {
        if (id != null) {
            getView()?.showProgress()
            addDisposable(getDetailContent(id)
                    .doOnSuccess { fullDetailContent = it }
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ showDetailContent(it) }, { onLoadDetailError(it, getErrorMessageId()) }))
        }
    }

    abstract fun getDetailContent(id: Long): Single<FullDetailContent<I>>

    protected open fun showDetailContent(fullDetailContent: FullDetailContent<I>) {
        getView()?.apply {
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
            getView()?.showImageList(imageUrlList)
        }
    }

    private fun addImages(urlList: ArrayList<String>, imageItemList: List<ImageItem>?,
                          getImageUrl: (String?) -> String?) {
        if (imageItemList.isNotNullOrEmpty()) {
            val posterImageUrlList = imageItemList!!
                    .map { getImageUrl(it.filePath) }
                    .filterNotNull()
                    .toList()
            urlList.addAll(posterImageUrlList)
        }
    }

    abstract fun getContentList(fullDetailContent: FullDetailContent<I>): List<String>

    abstract fun getBackdropImages(detailContent: I): List<ImageItem>?

    abstract fun getPosterImages(detailContent: I): List<ImageItem>?

    abstract fun getCredits(detailContent: I): CreditResults?

    private fun showCredits(creditResults: CreditResults?) {
        getView()?.apply {
            showItemList(creditResults?.cast) { showCastList(it) }
            showItemList(creditResults?.crew) { showCrewList(it) }
        }
    }

    private fun onLoadDetailError(t: Throwable, messageId: Int) {
        Logger.e(t)
        getView()?.apply {
            showErrorToast(t, messageId)
            finishActivity()
        }
    }

    abstract fun getErrorMessageId(): Int

    private fun showErrorToast(t: Throwable, messageId: Int) {
        getView()?.apply {
            if (t is IOException) {
                showToastMessage(R.string.error_no_internet)
            } else if (t is AuthException) {
                showToastMessage(R.string.error_not_logged_in)
            } else {
                showToastMessage(messageId)
            }
        }
    }

    protected fun <T> showItemList(itemList: List<T>?, showData: (List<T>) -> Unit) {
        if (itemList != null && itemList.isNotEmpty()) showData(itemList)
    }
}