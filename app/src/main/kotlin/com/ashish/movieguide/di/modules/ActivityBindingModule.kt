package com.ashish.movieguide.di.modules

import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.ui.episode.EpisodeDetailActivity
import com.ashish.movieguide.ui.episode.EpisodeDetailModule
import com.ashish.movieguide.ui.login.LoginActivity
import com.ashish.movieguide.ui.main.MainActivity
import com.ashish.movieguide.ui.main.MainModule
import com.ashish.movieguide.ui.movie.detail.MovieDetailActivity
import com.ashish.movieguide.ui.movie.detail.MovieDetailModule
import com.ashish.movieguide.ui.multisearch.activity.MultiSearchActivity
import com.ashish.movieguide.ui.multisearch.activity.MultiSearchModule
import com.ashish.movieguide.ui.people.detail.PersonDetailActivity
import com.ashish.movieguide.ui.profile.ProfileActivity
import com.ashish.movieguide.ui.profile.ProfileModule
import com.ashish.movieguide.ui.review.ReviewActivity
import com.ashish.movieguide.ui.season.SeasonDetailActivity
import com.ashish.movieguide.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movieguide.ui.tvshow.detail.TVShowDetailModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("unused")
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ProfileModule::class])
    abstract fun contributeProfileActivity(): ProfileActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [MovieDetailModule::class])
    abstract fun contributeMovieDetailActivity(): MovieDetailActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [TVShowDetailModule::class])
    abstract fun contributeTVShowDetailActivity(): TVShowDetailActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeSeasonDetailActivity(): SeasonDetailActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [EpisodeDetailModule::class])
    abstract fun contributeEpisodeDetailActivity(): EpisodeDetailActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributePersonDetailActivity(): PersonDetailActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [MultiSearchModule::class])
    abstract fun contributeMultiSearchActivity(): MultiSearchActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeReviewActivity(): ReviewActivity
}