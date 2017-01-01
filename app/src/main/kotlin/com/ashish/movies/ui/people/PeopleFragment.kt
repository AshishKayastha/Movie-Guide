package com.ashish.movies.ui.people

import android.os.Bundle
import android.view.View
import com.ashish.movies.R
import com.ashish.movies.data.models.People
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter.Companion.ADAPTER_TYPE_PEOPLE

/**
 * Created by Ashish on Dec 31.
 */
class PeopleFragment : BaseRecyclerViewFragment<People, BaseRecyclerViewMvpView<People>, PeoplePresenter>() {

    companion object {
        fun newInstance() = PeopleFragment()
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(PeopleModule()).inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyTextView.setText(R.string.no_people_available)
        emptyImageView.setImageResource(R.drawable.ic_people_white_100dp)
    }

    override fun getAdapterType() = ADAPTER_TYPE_PEOPLE

    override fun onItemClick(position: Int) {

    }
}