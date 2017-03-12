package com.ashish.movieguide.utils.extensions

import com.ashish.movieguide.data.api.tmdb.OMDbApi
import com.ashish.movieguide.data.models.tmdb.FullDetailContent
import com.ashish.movieguide.data.models.tmdb.OMDbDetail
import io.reactivex.Single

/**
 * Created by Ashish on Jan 09.
 */
fun <I> OMDbApi.convertToFullDetailContent(imdbId: String?, detailContent: I): Single<FullDetailContent<I>> {
    if (imdbId.isNotNullOrEmpty()) {
        return getDetailFromIMDbId(imdbId!!)
                .flatMap({ getFullDetailContent(detailContent, it) })
                .onErrorReturnItem(FullDetailContent(detailContent, null))
    } else {
        return getFullDetailContent(detailContent, null)
    }
}

private fun <I> getFullDetailContent(detailContent: I, omdbDetail: OMDbDetail?): Single<FullDetailContent<I>> {
    return Single.just(FullDetailContent(detailContent, omdbDetail))
}