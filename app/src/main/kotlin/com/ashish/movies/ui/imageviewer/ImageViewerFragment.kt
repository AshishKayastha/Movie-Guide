package com.ashish.movies.ui.imageviewer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.ui.common.palette.PaletteBitmap
import com.ashish.movies.ui.widget.TouchImageView
import com.ashish.movies.utils.extensions.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget

/**
 * Created by Ashish on Jan 08.
 */
class ImageViewerFragment : Fragment() {

    private val imageView: TouchImageView by bindView(R.id.image_view)
    private val progressBar: ProgressBar by bindView(R.id.progress_bar)

    private var imageUrl: String? = null

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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_image_viewer)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            imageUrl = arguments?.getString(ARG_IMAGE_URL)
        }

        if (imageUrl.isNotNullOrEmpty()) {
            progressBar.show()
            Glide.with(activity)
                    .transcodePaletteBitmap(activity)
                    .load(imageUrl)
                    .into(object : SimpleTarget<PaletteBitmap>() {
                        override fun onResourceReady(paletteBitmap: PaletteBitmap?,
                                                     animation: GlideAnimation<in PaletteBitmap>?) {
                            imageView.setImageBitmap(paletteBitmap?.bitmap)
                            progressBar.hide()

                            paletteBitmap?.palette.getSwatchWithMostPixels()?.apply {
                                (activity as ImageViewerActivity).animateViewPagerColorChange(rgb)
                            }
                        }
                    })
        }
    }

    override fun onDestroyView() {
        Glide.clear(imageView)
        super.onDestroyView()
    }
}