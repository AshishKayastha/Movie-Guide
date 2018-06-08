package com.ashish.movieguide.ui.base.detail

import com.ashish.movieguide.data.network.entities.common.OMDbDetail
import com.ashish.movieguide.data.network.entities.tmdb.Credit
import com.ashish.movieguide.ui.base.mvp.ProgressMvpView
import java.util.ArrayList

/**
 * Created by Ashish on Jan 03.
 */
interface BaseDetailView<in I> : ProgressMvpView {

    fun showDetailContent(detailContent: I)

    fun showDetailContentList(contentList: List<String>)

    fun showImageList(imageUrlList: ArrayList<String>)

    fun showOMDbDetail(omDbDetail: OMDbDetail)

    fun showCastList(castList: List<Credit>)

    fun showCrewList(crewList: List<Credit>)

    fun finishActivity()
}