package com.ashish.movieguide.ui.review

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.Review
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RemoveListener
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.common.adapter.ViewTypeDelegateAdapter
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.inflate

class ReviewDelegateAdapter(
        private val layoutId: Int,
        private var onItemClickListener: OnItemClickListener?
) : ViewTypeDelegateAdapter, RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup) = ReviewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as ReviewHolder).bindData(item as Review)
    }

    override fun removeListener() {
        onItemClickListener = null
    }

    inner class ReviewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(layoutId)) {

        private val reviewAuthorText: FontTextView by bindView(R.id.review_author)
        private val reviewContentText: FontTextView by bindView(R.id.review_content)

        init {
            itemView.setOnClickListener { view ->
                onItemClickListener?.onItemClick(adapterPosition, view)
            }
        }

        fun bindData(review: Review) {
            reviewAuthorText.applyText(review.author)
            reviewContentText.applyText(review.content)
        }
    }
}