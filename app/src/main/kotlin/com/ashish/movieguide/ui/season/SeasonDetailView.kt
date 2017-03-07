package com.ashish.movieguide.ui.season

import com.ashish.movieguide.data.models.Episode
import com.ashish.movieguide.data.models.SeasonDetail
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentView

/**
 * Created by Ashish on Jan 08.
 */
interface SeasonDetailView : FullDetailContentView<SeasonDetail> {

    fun showEpisodeList(episodeList: List<Episode>)
}