package com.ashish.movieguide.ui.people.list

import android.content.Intent
import com.ashish.movieguide.R
import com.ashish.movieguide.data.remote.entities.tmdb.Person
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.people.detail.PersonDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_PERSON
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class PeopleFragment : RecyclerViewFragment<Person, RecyclerViewMvpView<Person>, PeoplePresenter>() {

    companion object {
        fun newInstance() = PeopleFragment()
    }

    @Inject lateinit var peoplePresenter: PeoplePresenter

    override fun providePresenter(): PeoplePresenter = peoplePresenter

    override fun getEmptyTextId(): Int = R.string.no_people_available

    override fun getEmptyImageId(): Int = R.drawable.ic_people_white_100dp

    override fun getAdapterType(): Int = ADAPTER_TYPE_PERSON

    override fun getDetailIntent(position: Int): Intent? {
        val people = recyclerViewAdapter.getItem<Person>(position)
        return PersonDetailActivity.createIntent(activity!!, people)
    }

    override fun getTransitionNameId(position: Int): Int = R.string.transition_person_profile
}