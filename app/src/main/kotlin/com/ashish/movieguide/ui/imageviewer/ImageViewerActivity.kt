package com.ashish.movieguide.ui.imageviewer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.transition.TransitionInflater
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.base.common.BaseActivity
import com.ashish.movieguide.ui.widget.DepthPageTransformer
import com.ashish.movieguide.utils.SystemUiHelper
import com.ashish.movieguide.utils.extensions.find
import com.ashish.movieguide.utils.transition.LeakFreeSupportSharedElementCallback
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.activity_image_viewer.*
import java.util.ArrayList

/**
 * Created by Ashish on Jan 08.
 */
class ImageViewerActivity : BaseActivity() {

    companion object {
        const val SHOW_UI_MILLIS = 4000L
        const val EXTRA_CURRENT_POSITION = "current_position"
        private const val EXTRA_IMAGE_URL_LIST = "image_url_list"

        fun createIntent(
                context: Context,
                currentPosition: Int,
                imageUrlList: ArrayList<String>
        ): Intent = Intent(context, ImageViewerActivity::class.java)
                .putExtra(EXTRA_IMAGE_URL_LIST, imageUrlList)
                .putExtra(EXTRA_CURRENT_POSITION, currentPosition)
    }

    @State var currentPosition: Int = 0
    @State var imageUrlList: ArrayList<String>? = null

    var systemUiHelper: SystemUiHelper? = null
    private lateinit var imageViewerAdapter: ImageViewerAdapter

    private val callback = object : LeakFreeSupportSharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
            imageViewerAdapter.getRegisteredFragment(currentPosition)?.view?.let {
                sharedElements[names[0]] = it.find(R.id.imageView)
            }
        }
    }

    // Show or hide app bar depending upon whether SystemUI is currently visible or not
    private val visibilityChangeListener = object : SystemUiHelper.OnVisibilityChangeListener {
        override fun onVisibilityChange(visible: Boolean) {
            appBarWrapper.animate()
                    .alpha(if (visible) 1f else 0f)
                    .translationY(if (visible) 0f else -appBar.bottom.toFloat())
                    .setDuration(400L)
                    .setInterpolator(FastOutSlowInInterpolator())
                    .start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.sharedElementEnterTransition = TransitionInflater.from(this)
                .inflateTransition(R.transition.image_shared_element_transition)
        setEnterSharedElementCallback(callback)
        if (savedInstanceState == null) postponeEnterTransition()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setImageCountTitle()
        }

        imageViewerAdapter = ImageViewerAdapter(supportFragmentManager, imageUrlList!!)
        viewPager.apply {
            adapter = imageViewerAdapter
            currentItem = currentPosition
            setPageTransformer(true, DepthPageTransformer())
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                currentPosition = position
                setImageCountTitle()
            }
        })

        systemUiHelper = SystemUiHelper(this, listener = visibilityChangeListener)
    }

    override fun getLayoutId() = R.layout.activity_image_viewer

    override fun getIntentExtras(extras: Bundle?) {
        currentPosition = extras?.getInt(EXTRA_CURRENT_POSITION) ?: 0
        imageUrlList = extras?.getStringArrayList(EXTRA_IMAGE_URL_LIST)
    }

    private fun setImageCountTitle() {
        supportActionBar?.title = "${currentPosition + 1} of ${imageUrlList!!.size}"
    }

    override fun onStart() {
        super.onStart()
        systemUiHelper?.show()
        systemUiHelper?.delayHide(SHOW_UI_MILLIS)
    }

    override fun finishAfterTransition() {
        val data = Intent()
        data.putExtra(EXTRA_CURRENT_POSITION, currentPosition)
        setResult(Activity.RESULT_OK, data)
        super.finishAfterTransition()
    }

    override fun onDestroy() {
        viewPager.clearOnPageChangeListeners()
        systemUiHelper?.removeVisibilityChangeListener()
        super.onDestroy()
    }
}