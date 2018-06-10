package com.ashish.movieguide.ui.imageviewer

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.base.common.BaseFragment
import com.ashish.movieguide.utils.Constants.DETAIL_IMAGE_THUMBNAIL_SIZE
import com.ashish.movieguide.utils.StartTransitionListener
import com.ashish.movieguide.utils.SystemUiHelper
import com.ashish.movieguide.utils.extensions.convertToOriginalImageUrl
import com.ashish.movieguide.utils.glide.GlideApp
import com.ashish.movieguide.utils.glide.LoggingListener
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.fragment_image_viewer.*
import timber.log.Timber

/**
 * Created by Ashish on Jan 08.
 */
class ImageViewerFragment : BaseFragment() {

    companion object {
        private const val ARG_POSITION = "position"
        private const val ARG_IMAGE_URL = "image_url"

        fun newInstance(position: Int, imageUrl: String?): ImageViewerFragment {
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            args.putString(ARG_IMAGE_URL, imageUrl)
            val fragment = ImageViewerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @State var position: Int = 0
    @State var imageUrl: String? = null

    private var fullBitmapRequest: RequestBuilder<Bitmap>? = null
    private var thumbBitmapRequest: RequestBuilder<Bitmap>? = null

    override fun getLayoutId() = R.layout.fragment_image_viewer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGlide()
        setupImage()
        handleTouchEvent()
    }

    override fun getFragmentArguments(arguments: Bundle?) {
        imageUrl = arguments?.getString(ARG_IMAGE_URL)
        position = arguments?.getInt(ARG_POSITION) ?: 0
    }

    private fun setupGlide() {
        thumbBitmapRequest = GlideApp.with(this)
                .asBitmap()
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions().override(DETAIL_IMAGE_THUMBNAIL_SIZE, DETAIL_IMAGE_THUMBNAIL_SIZE))

        fullBitmapRequest = GlideApp.with(this)
                .asBitmap()
                .load(imageUrl!!.convertToOriginalImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
    }

    private fun setupImage() {
        imageView.transitionName = "image_$position"
        loadThumbnail()
        fullBitmapRequest?.preload()
        loadFullImage()
    }

    @SuppressLint("CheckResult")
    fun loadThumbnail() {
        thumbBitmapRequest?.listener(StartTransitionListener<Bitmap>(activity!!))
        thumbBitmapRequest?.into(imageView)
    }

    private fun loadFullImage() {
        fullBitmapRequest
                ?.thumbnail(thumbBitmapRequest)
                ?.listener(LoggingListener<Bitmap>())
                ?.into(imageView)
    }

    /**
     * Returns the shared element that should be transitioned back to the
     * previous Activity, or null if the view is not visible on the screen.
     */
    fun getImageView(): ImageView? {
        return if (isViewInBounds(activity?.window?.decorView, imageView)) imageView else null
    }

    /**
     * Returns true if {@param view} is contained within {@param container}'s bounds.
     */
    private fun isViewInBounds(container: View?, view: View): Boolean {
        val containerBounds = Rect()
        if (container != null) {
            container.getHitRect(containerBounds)
            return view.getLocalVisibleRect(containerBounds)
        }
        return false
    }

    private fun handleTouchEvent() {
        if (activity != null) {
            val detector = GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(event: MotionEvent) = true

                override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
                    Timber.v("onSingleTapConfirmed")
                    toggleSystemUiVisibility()
                    return true
                }
            })

            imageContainer.setOnTouchListener { _, event ->
                Timber.v("setOnTouchListener")
                detector.onTouchEvent(event)
            }
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
        imageView.setOnTouchListener(null)
        super.onDestroyView()
    }
}