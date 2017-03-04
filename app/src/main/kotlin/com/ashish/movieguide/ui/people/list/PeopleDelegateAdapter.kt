package com.ashish.movieguide.ui.people.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movieguide.data.models.Person
import com.ashish.movieguide.ui.base.recyclerview.BaseContentHolder
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RemoveListener
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.common.adapter.ViewTypeDelegateAdapter
import com.ashish.movieguide.utils.extensions.getProfileUrl
import com.ashish.movieguide.utils.extensions.hide

/**
 * Created by Ashish on Dec 31.
 */
class PeopleDelegateAdapter(
        private val layoutId: Int,
        private var onItemClickListener: OnItemClickListener?
) : ViewTypeDelegateAdapter, RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup) = PeopleHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as PeopleHolder).bindData(item as Person)
    }

    override fun removeListener() {
        onItemClickListener = null
    }

    inner class PeopleHolder(parent: ViewGroup) : BaseContentHolder<Person>(parent, layoutId) {

        override fun bindData(item: Person) = with(item) {
            contentTitle.text = name
            contentSubtitle.hide()
            super.bindData(item)
        }

        override fun getItemClickListener() = onItemClickListener

        override fun getImageUrl(item: Person) = item.profilePath.getProfileUrl()
    }
}