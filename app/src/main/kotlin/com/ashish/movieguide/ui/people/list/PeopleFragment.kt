package com.ashish.movieguide.ui.people.list

import android.content.Intent
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.Person
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.people.detail.PersonDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_PERSON
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class PeopleFragment : BaseRecyclerViewFragment<Person,
        BaseRecyclerViewMvpView<Person>, PeoplePresenter>() {

    companion object {
        fun newInstance() = PeopleFragment()
    }

    @Inject lateinit var peoplePresenter: PeoplePresenter

    override fun providePresenter(): PeoplePresenter = peoplePresenter

    override fun getAdapterType() = ADAPTER_TYPE_PERSON

    override fun getEmptyTextId() = R.string.no_people_available

    override fun getEmptyImageId() = R.drawable.ic_people_white_100dp

    override fun getTransitionNameId(position: Int) = R.string.transition_person_profile

    override fun getDetailIntent(position: Int): Intent? {
        return if (activity != null) {
            val people = recyclerViewAdapter.getItem<Person>(position)
            PersonDetailActivity.createIntent(activity!!, people)
        } else null
    }
}