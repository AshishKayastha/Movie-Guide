package com.ashish.movieguide.ui.episode

import com.ashish.movieguide.data.network.entities.tmdb.EpisodeDetail
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentView
import com.ashish.movieguide.ui.common.rating.RatingMvpView

interface EpisodeDetailView : FullDetailContentView<EpisodeDetail>, RatingMvpView