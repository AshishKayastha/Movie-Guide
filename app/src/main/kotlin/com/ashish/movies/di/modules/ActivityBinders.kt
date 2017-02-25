package com.ashish.movies.di.modules

import com.ashish.movies.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movies.di.multibindings.activity.ActivityKey
import com.ashish.movies.ui.main.MainActivity
import com.ashish.movies.ui.main.MainComponent
import com.ashish.movies.ui.movie.detail.MovieDetailActivity
import com.ashish.movies.ui.movie.detail.MovieDetailComponent
import com.ashish.movies.ui.multisearch.activity.MultiSearchActivity
import com.ashish.movies.ui.multisearch.activity.MultiSearchComponent
import com.ashish.movies.ui.people.detail.PersonDetailActivity
import com.ashish.movies.ui.people.detail.PersonDetailComponent
import com.ashish.movies.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movies.ui.tvshow.detail.TVShowDetailComponent
import com.ashish.movies.ui.tvshow.episode.EpisodeDetailActivity
import com.ashish.movies.ui.tvshow.episode.EpisodeDetailComponent
import com.ashish.movies.ui.tvshow.season.SeasonDetailActivity
import com.ashish.movies.ui.tvshow.season.SeasonDetailComponent
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