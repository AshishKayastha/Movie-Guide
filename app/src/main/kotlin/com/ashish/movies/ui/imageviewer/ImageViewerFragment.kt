package com.ashish.movies.ui.imageviewer

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.ProgressBar
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.ui.base.common.BaseFragment
import com.ashish.movies.ui.widget.TouchImageView
import com.ashish.movies.utils.extensions.hide
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.show
import com.ashish.movies.utils.systemuihelper.SystemUiHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import icepick.State

/**
 * Created by Ashish on Jan 08.
 */
class ImageViewerFragment : BaseFragment() {

    @JvmField @State var position: Int = 0
    @JvmField @State var imageUrl: String? = null
    @JvmField @State var startingPosition: Int = 0

    private val imageView: TouchImageView by bindView(R.id.image_view)
    private val progressBar: ProgressBar by bindView(R.id.progress_bar)

    companion object {
        private const val ARG_POSITION = "position"
        private const val ARG_IMAGE_URL = "image_url"
        private const val ARG_STARTING_POSITION = "starting_position"

        fun newInstance(position: Int, startingPosition: Int, imageUrl: String?): ImageViewerFragment {
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            args.putString(ARG_IMAGE_URL, imageUrl)
            args.putInt(ARG_STARTING_POSITION, startingPosition)
            val fragment = ImageViewerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutId() = R.layout.fragment_image_viewer

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView.transitionName = "image_$position"
        loadImage()
        handleTouchEvent()
    }

    override fun getFragmentArguments(arguments: Bundle?) {
        imageUrl = arguments?.getString(ARG_IMAGE_URL)
        position = arguments?.getInt(ARG_POSITION) ?: 0
    }

    private fun loadImage() {
        if (imageUrl.isNotNullOrEmpty()) {
            progressBar.show()
            Glide.with(activity)
                    .load(imageUrl)
                    .asBitmap()
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(bitmap: Bitmap?, animation: GlideAnimation<in Bitmap>?) {
                            progressBar.hide()
                            if (bitmap != null) imageView.setImageBitmap(bitmap)
                            startEnterTransition()
                        }

                        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                            startEnterTransition()
                        }
                    })
        }
    }

    private fun startEnterTransition() {
        if (position == startingPosition) {
            imageView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    imageView.viewTreeObserver.removeOnPreDrawListener(this)
                    activity?.startPostponedEnterTransition()
                    return true
                }
            })
        }
    }

    fun getImageView(): ImageView? {
        if (isViewInBounds(activity.window.decorView, imageView)) {
            return imageView
        }
        return null
    }

    private fun isViewInBounds(container: View, view: View): Boolean {
        val containerBounds = Rect()
        container.getHitRect(containerBounds)
        return view.getLocalVisibleRect(containerBounds)
    }

    private fun handleTouchEvent() {
        if (activity != null) {
            val detector = GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(event: MotionEvent) = true

                override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
                    toggleSystemUiVisibility()
                    return true
                }
            })

            imageView.setOnTouchListener { view, event -> detector.onTouchEvent(event) }
        }
    }

    private fun toggleSystemUiVisibility() {
        val systemUiHelper = getSystemUiHelper()
        if (systemUiHelper != null) {
            if (systemUiHelper.isShowing) {
                systemUiHelper.hide()
            } else {
                systemUiHelper.show()
                systemUiHelper.delayHide(ImageViewerActivity.SHOW_UI_MILLIS)
            }
        }
    }

    private fun getSystemUiHelper(): SystemUiHelper? {
        return if (activity != null) (activity as ImageViewerActivity).systemUiHelper else null
    }

    override fun onDestroyView() {
        Glide.clear(imageView)
        super.onDestroyView()
    }
}