package com.ashish.movies.ui.people.detail

import com.ashish.movies.data.interactors.PeopleInteractor
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Jan 04.
 */
@Module
class PersonDetailModule {

    @Provides
    fun providePersonDetailPresenter(peopleInteractor: PeopleInteractor) = PersonDetailPresenter(peopleInteractor)
}