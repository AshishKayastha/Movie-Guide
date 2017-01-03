package com.ashish.movies.ui.people.list

import dagger.Subcomponent

/**
 * Created by Ashish on Dec 31.
 */
@Subcomponent(modules = arrayOf(PeopleModule::class))
interface PeopleSubComponent {

    fun inject(peopleFragment: PeopleFragment)
}