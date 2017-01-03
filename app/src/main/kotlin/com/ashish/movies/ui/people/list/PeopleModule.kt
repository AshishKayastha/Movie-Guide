package com.ashish.movies.ui.people.list

import com.ashish.movies.data.interactors.PeopleInteractor
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Dec 31.
 */
@Module
class PeopleModule {

    @Provides
    fun providePeoplePresenter(peopleInteractor: PeopleInteractor) = PeoplePresenter(peopleInteractor)
}