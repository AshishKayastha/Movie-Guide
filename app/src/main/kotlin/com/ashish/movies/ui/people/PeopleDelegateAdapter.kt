package com.ashish.movies.ui.people

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movies.data.models.People
import com.ashish.movies.ui.base.recyclerview.BaseContentHolder
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.adapter.ViewTypeDelegateAdapter
import com.ashish.movies.utils.Constants.PROFILE_ORIGINAL_URL_PREFIX

/**
 * Created by Ashish on Dec 31.
 */
class PeopleDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = PeopleHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as PeopleHolder).bindData(item as People)
    }

    inner class PeopleHolder(parent: ViewGroup) : BaseContentHolder<People>(parent) {

        override fun bindData(item: People) = with(item) {
            contentTitle.text = name
            super.bindData(item)
        }

        override fun getImageUrl(item: People) = PROFILE_ORIGINAL_URL_PREFIX + item.profilePath
    }
}