package com.ashish.movies.ui.base.detail

import com.ashish.movies.data.models.Credit
import com.ashish.movies.ui.base.mvp.LceView

/**
 * Created by Ashish on Jan 03.
 */
interface BaseDetailMvpView<in I> : LceView {

    fun showDetailContent(detailContent: I?)

    fun showCastList(castList: List<Credit>)

    fun showCrewList(crewList: List<Credit>)
}