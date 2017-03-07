package com.ashish.movieguide.ui.people.list

import android.content.Intent
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.Person
import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.people.detail.PersonDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_PERSON

/**
 * Created by Ashish on Dec 31.
 */
class PeopleFragment : BaseRecyclerViewFragment<Person,
        BaseRecyclerViewMvpView<Person>, PeoplePresenter>() {

    companion object {
        @JvmStatic fun newInstance() = PeopleFragment()
    }

    override fun injectDependencies(builderHost: FragmentComponentBuilderHost) {
        builderHost.getFragmentComponentBuilder(PeopleFragment::class.java, PeopleComponent.Builder::class.java)
                .withModule(FragmentModule(activity))
                .build()
                .inject(this)
    }

    override fun getAdapterType() = ADAPTER_TYPE_PERSON

    override fun getEmptyTextId() = R.string.no_people_available

    override fun getEmptyImageId() = R.drawable.ic_people_white_100dp

    override fun getTransitionNameId(position: Int) = R.string.transition_person_profile

    override fun getDetailIntent(position: Int): Intent? {
        val people = recyclerViewAdapter.getItem<Person>(position)
        return PersonDetailActivity.createIntent(activity, people)
    }
}