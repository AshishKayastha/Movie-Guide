package com.ashish.movies.ui.people.detail

import dagger.Subcomponent

/**
 * Created by Ashish on Jan 04.
 */
@Subcomponent(modules = arrayOf(PersonDetailModule::class))
interface PersonDetailSubComponent {

    fun inject(personDetailActivity: PersonDetailActivity)
}