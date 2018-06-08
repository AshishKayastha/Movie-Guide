package com.ashish.movieguide.ui.people.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movieguide.data.network.entities.tmdb.Person
import com.ashish.movieguide.ui.base.recyclerview.BaseContentHolder
import com.ashish.movieguide.ui.base.recyclerview.ContentDelegateAdapter
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.utils.extensions.getProfileUrl
import com.ashish.movieguide.utils.extensions.hide

/**
 * Created by Ashish on Dec 31.
 */
class PersonDelegateAdapter(
        layoutId: Int,
        onItemClickListener: OnItemClickListener?
) : ContentDelegateAdapter(layoutId, onItemClickListener) {

    override fun getHolder(parent: ViewGroup, layoutId: Int) = PersonHolder(parent, layoutId)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as PersonHolder).bindData(item as Person)
    }

    class PersonHolder(parent: ViewGroup, layoutId: Int) : BaseContentHolder<Person>(parent, layoutId) {

        override fun bindData(item: Person) {
            with(item) {
                super.bindData(item)
                contentTitle.text = name
                contentSubtitle.hide()
            }
        }

        override fun getImageUrl(item: Person): String? = item.profilePath.getProfileUrl()
    }
}