package com.ashish.movies.ui.people.detail

import com.ashish.movies.data.interactors.PeopleInteractor
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Jan 04.
 */
@Module
class PeopleDetailModule {

    @Provides
    fun providePeopleDetailPresenter(peopleInteractor: PeopleInteractor) = PeopleDetailPresenter(peopleInteractor)
}