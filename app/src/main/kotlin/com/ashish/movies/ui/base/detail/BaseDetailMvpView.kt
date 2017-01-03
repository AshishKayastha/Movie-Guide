package com.ashish.movies.ui.base.detail

import com.ashish.movies.data.models.Credit
import com.ashish.movies.ui.base.mvp.LceView
import com.ashish.movies.ui.common.adapter.ViewType

/**
 * Created by Ashish on Jan 03.
 */
interface BaseDetailMvpView<in I : ViewType> : LceView {

    fun showDetailContent(item: I?)

    fun showCastList(castList: List<Credit>)

    fun showCrewList(crewList: List<Credit>)

    fun showSimilarContentList(similarItemList: List<I>)
}