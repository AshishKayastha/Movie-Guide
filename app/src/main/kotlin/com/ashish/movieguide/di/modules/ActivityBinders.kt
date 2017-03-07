package com.ashish.movieguide.di.modules

import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movieguide.di.multibindings.activity.ActivityKey
import com.ashish.movieguide.ui.episode.EpisodeDetailActivity
import com.ashish.movieguide.ui.episode.EpisodeDetailComponent
import com.ashish.movieguide.ui.main.MainActivity
import com.ashish.movieguide.ui.main.MainComponent
import com.ashish.movieguide.ui.movie.detail.MovieDetailActivity
import com.ashish.movieguide.ui.movie.detail.MovieDetailComponent
import com.ashish.movieguide.ui.multisearch.activity.MultiSearchActivity
import com.ashish.movieguide.ui.multisearch.activity.MultiSearchComponent
import com.ashish.movieguide.ui.people.detail.PersonDetailActivity
import com.ashish.movieguide.ui.people.detail.PersonDetailComponent
import com.ashish.movieguide.ui.season.SeasonDetailActivity
import com.ashish.movieguide.ui.season.SeasonDetailComponent
import com.ashish.movieguide.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movieguide.ui.tvshow.detail.TVShowDetailComponent
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Ashish on Feb 25.
 */
@Module(subcomponents = arrayOf(
        MainComponent::class,
        MovieDetailComponent::class,
        TVShowDetailComponent::class,
        SeasonDetailComponent::class,
        EpisodeDetailComponent::class,
        PersonDetailComponent::class,
        MultiSearchComponent::class
))
abstract class ActivityBinders {

    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    abstract fun mainComponentBuilder(builder: MainComponent.Builder): ActivityComponentBuilder<*, *>

    @Binds
    @IntoMap
    @ActivityKey(MovieDetailActivity::class)
    abstract fun movieDetailComponentBuilder(builder: MovieDetailComponent.Builder): ActivityComponentBuilder<*, *>

    @Binds
    @IntoMap
    @ActivityKey(TVShowDetailActivity::class)
    abstract fun tvShowDetailComponentBuilder(builder: TVShowDetailComponent.Builder): ActivityComponentBuilder<*, *>

    @Binds
    @IntoMap
    @ActivityKey(SeasonDetailActivity::class)
    abstract fun seasonDetailComponentBuilder(builder: SeasonDetailComponent.Builder): ActivityComponentBuilder<*, *>

    @Binds
    @IntoMap
    @ActivityKey(EpisodeDetailActivity::class)
    abstract fun episodeDetailComponentBuilder(builder: EpisodeDetailComponent.Builder): ActivityComponentBuilder<*, *>

    @Binds
    @IntoMap
    @ActivityKey(PersonDetailActivity::class)
    abstract fun personDetailComponentBuilder(builder: PersonDetailComponent.Builder): ActivityComponentBuilder<*, *>

    @Binds
    @IntoMap
    @ActivityKey(MultiSearchActivity::class)
    abstract fun multiSearchComponentBuilder(builder: MultiSearchComponent.Builder): ActivityComponentBuilder<*, *>
}