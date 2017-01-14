package com.ashish.movies.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.utils.extensions.inflate
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * Created by Ashish on Jan 14.
 */
class ImageAdapter(val imageUrlList: List<String>) : RecyclerView.Adapter<ImageAdapter.ImageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ImageHolder {
        return ImageHolder(parent?.inflate(R.layout.list_item_detail_image)!!)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bindData(imageUrlList[position])
    }

    override fun getItemCount() = imageUrlList.size

    override fun onViewRecycled(holder: ImageHolder) {
        super.onViewRecycled(holder)
        Glide.clear(holder.imageView)
    }

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView by bindView(R.id.detail_content_image)

        fun bindData(imageUrl: String) {
            if (imageUrl.isNotNullOrEmpty()) {
                Glide.with(imageView.context)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(imageView)
            } else {
                Glide.clear(imageView)
            }
        }
    }
}