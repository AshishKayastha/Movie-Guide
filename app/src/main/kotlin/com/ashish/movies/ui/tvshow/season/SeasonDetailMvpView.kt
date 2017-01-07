package com.ashish.movies.ui.tvshow.season

import com.ashish.movies.data.models.Episode
import com.ashish.movies.data.models.SeasonDetail
import com.ashish.movies.ui.base.detail.BaseDetailMvpView

/**
 * Created by Ashish on Jan 08.
 */
interface SeasonDetailMvpView : BaseDetailMvpView<SeasonDetail> {

    fun showEpisodeList(episodeList: List<Episode>)
}