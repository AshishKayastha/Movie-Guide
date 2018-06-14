package com.ashish.movieguide.ui.people.list

import android.support.v7.widget.RecyclerView
import android.view.View
import com.ashish.movieguide.data.remote.entities.tmdb.Person
import com.ashish.movieguide.ui.base.adapter.BaseContentHolder
import com.ashish.movieguide.ui.base.adapter.ContentDelegateAdapter
import com.ashish.movieguide.ui.base.adapter.RecyclerViewItem
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.utils.extensions.getProfileUrl
import com.ashish.movieguide.utils.extensions.hide

/**
 * Created by Ashish on Dec 31.
 */
class PersonDelegateAdapter(
        layoutId: Int,
        onItemClickListener: OnItemClickListener?
) : ContentDelegateAdapter(layoutId, onItemClickListener) {

    override fun getHolder(view: View) = PersonHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: RecyclerViewItem) {
        (holder as PersonHolder).bindData(item as Person)
    }

    class PersonHolder(view: View) : BaseContentHolder<Person>(view) {

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