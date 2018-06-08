package com.ashish.movieguide.ui.review

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.Review
import com.ashish.movieguide.ui.base.recyclerview.BaseHolder
import com.ashish.movieguide.ui.base.recyclerview.ContentDelegateAdapter
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.widget.CircularTextDrawable
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.hide
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.show

class ReviewDelegateAdapter(
        layoutId: Int,
        onItemClickListener: OnItemClickListener?
) : ContentDelegateAdapter(layoutId, onItemClickListener) {

    override fun getHolder(parent: ViewGroup, layoutId: Int) = ReviewHolder(parent, layoutId)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as ReviewHolder).bindData(item as Review)
    }

    class ReviewHolder(parent: ViewGroup, layoutId: Int) : BaseHolder(parent, layoutId) {

        private val reviewAuthorText: FontTextView by bindView(R.id.review_author)
        private val reviewContentText: FontTextView by bindView(R.id.review_content)
        private val reviewAuthorIcon: ImageView  by bindView(R.id.review_author_icon)

        fun bindData(review: Review) {
            with(review) {
                if (author.isNotNullOrEmpty()) {
                    reviewAuthorIcon.show()
                    val drawable = CircularTextDrawable(itemView.context, author!!.first().toString())
                    reviewAuthorIcon.setImageDrawable(drawable)
                } else {
                    reviewAuthorIcon.hide()
                }

                reviewAuthorText.applyText(author)
                reviewContentText.applyText(content)
            }
        }
    }
}