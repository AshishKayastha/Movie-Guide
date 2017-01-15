package com.ashish.movies.ui.imageviewer

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.ui.base.common.BaseFragment
import com.ashish.movies.ui.widget.TouchImageView
import com.ashish.movies.utils.extensions.hide
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.show
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import icepick.State

/**
 * Created by Ashish on Jan 08.
 */
class ImageViewerFragment : BaseFragment() {

    @JvmField @State var imageUrl: String? = null

    private val imageView: TouchImageView by bindView(R.id.image_view)
    private val progressBar: ProgressBar by bindView(R.id.progress_bar)

    companion object {
        private const val ARG_IMAGE_URL = "image_url"

        fun newInstance(imageUrl: String?): ImageViewerFragment {
            val args = Bundle()
            args.putString(ARG_IMAGE_URL, imageUrl)
            val fragment = ImageViewerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutId() = R.layout.fragment_image_viewer

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (imageUrl.isNotNullOrEmpty()) {
            progressBar.show()
            Glide.with(activity)
                    .load(imageUrl)
                    .asBitmap()
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(bitmap: Bitmap?, animation: GlideAnimation<in Bitmap>?) {
                            progressBar.hide()
                            if (bitmap != null) imageView.setImageBitmap(bitmap)
                        }
                    })
        }
    }

    override fun getFragmentArguments(arguments: Bundle?) {
        imageUrl = arguments?.getString(ARG_IMAGE_URL)
    }
}