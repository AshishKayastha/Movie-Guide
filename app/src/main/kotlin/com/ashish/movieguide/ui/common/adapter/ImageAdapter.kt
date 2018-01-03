package com.ashish.movieguide.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.Constants.DETAIL_IMAGE_THUMBNAIL_SIZE
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.inflate
import com.ashish.movieguide.utils.glide.GlideApp
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Ashish on Jan 14.
 */
class ImageAdapter(
        val imageUrlList: ArrayList<String>,
        private var onItemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<ImageAdapter.ImageHolder>(), RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ImageHolder {
        return ImageHolder(parent?.inflate(R.layout.list_item_detail_image)!!)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bindData(imageUrlList[position], position)
    }

    override fun getItemCount() = imageUrlList.size

    override fun removeListener() {
        onItemClickListener = null
    }

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView by bindView(R.id.detail_content_image)

        init {
            itemView.setOnClickListener { view ->
                onItemClickListener?.onItemClick(adapterPosition, view)
            }
        }

        fun bindData(imageUrl: String, position: Int) {
            GlideApp.with(imageView.context)
                    .asBitmap()
                    .apply(RequestOptions().override(DETAIL_IMAGE_THUMBNAIL_SIZE, DETAIL_IMAGE_THUMBNAIL_SIZE))
                    .load(imageUrl)
                    .into(imageView)

            imageView.transitionName = "image_$position"
        }
    }
}