package com.ashish.movies.ui.base.detail

import com.ashish.movies.data.models.Credit
import com.ashish.movies.data.models.OMDbDetail
import com.ashish.movies.ui.base.mvp.ProgressView
import java.util.ArrayList

/**
 * Created by Ashish on Jan 03.
 */
interface BaseDetailView<in I> : ProgressView {

    fun showDetailContent(detailContent: I)

    fun showDetailContentList(contentList: List<String>)

    fun showImageList(imageUrlList: ArrayList<String>)

    fun showTrailerFAB(trailerUrl: String)

    fun showOMDbDetail(omDbDetail: OMDbDetail)

    fun showCastList(castList: List<Credit>)

    fun showCrewList(crewList: List<Credit>)

    fun finishActivity()
}