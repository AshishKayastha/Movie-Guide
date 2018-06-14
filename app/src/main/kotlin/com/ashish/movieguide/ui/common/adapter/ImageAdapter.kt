package com.ashish.movieguide.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.base.adapter.BaseHolder
import com.ashish.movieguide.ui.base.adapter.RemoveListener
import com.ashish.movieguide.utils.Constants.DETAIL_IMAGE_OPTIONS
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.inflate
import com.ashish.movieguide.utils.glide.GlideApp
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * Created by Ashish on Jan 14.
 */
class ImageAdapter(
        val imageUrlList: ArrayList<String>,
        private var onItemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<ImageAdapter.ImageHolder>(), RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder(parent.inflate(R.layout.list_item_detail_image))
                .also { it.attachListener(onItemClickListener) }
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bindData(imageUrlList[position])
    }

    override fun getItemCount() = imageUrlList.size

    override fun removeListener() {
        onItemClickListener = null
    }

    class ImageHolder(view: View) : BaseHolder(view) {

        private val imageView: ImageView by bindView(R.id.detail_content_image)
        private val containerView: View by bindView(R.id.detail_image_container)

        override fun attachListener(onItemClickListener: OnItemClickListener?) {
            containerView.setOnClickListener { onItemClickListener?.onItemClick(adapterPosition, it) }
        }

        fun bindData(imageUrl: String) {
            GlideApp.with(imageView.context)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(DETAIL_IMAGE_OPTIONS)
                    .load(imageUrl)
                    .into(imageView)

            imageView.transitionName = "image_$adapterPosition"
        }
    }
}