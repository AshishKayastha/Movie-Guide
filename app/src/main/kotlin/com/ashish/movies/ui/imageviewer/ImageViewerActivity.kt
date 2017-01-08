package com.ashish.movies.ui.imageviewer

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.ui.base.common.BaseActivity
import com.ashish.movies.utils.extensions.animateBackgroundColorChange
import com.ashish.movies.utils.extensions.changeViewGroupTextFont
import java.util.*

/**
 * Created by Ashish on Jan 08.
 */
class ImageViewerActivity : BaseActivity() {

    private val viewPager: ViewPager by bindView(R.id.view_pager)

    private var title: String = ""
    private var imageUrlList: ArrayList<String>? = null

    companion object {
        private const val EXTRA_TITLE = "title"
        private const val EXTRA_IMAGE_URL_LIST = "image_url_list"

        fun createIntent(context: Context, title: String, imageUrlList: ArrayList<String>): Intent {
            return Intent(context, ImageViewerActivity::class.java)
                    .putExtra(EXTRA_TITLE, title)
                    .putExtra(EXTRA_IMAGE_URL_LIST, imageUrlList)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar?.changeViewGroupTextFont()

        if (savedInstanceState == null) {
            val extras = intent.extras
            title = extras?.getString(EXTRA_TITLE) ?: ""
            imageUrlList = extras?.getStringArrayList(EXTRA_IMAGE_URL_LIST)
        }

        supportActionBar?.apply {
            title = this@ImageViewerActivity.title
            setDisplayHomeAsUpEnabled(true)
        }

        viewPager.apply {
            adapter = ImageViewerAdapter(supportFragmentManager, imageUrlList)
            setPageTransformer(true, StackPageTransformer())
        }
    }

    override fun getLayoutId() = R.layout.activity_image_viewer

    fun animateViewPagerColorChange(color: Int) {
        viewPager.animateBackgroundColorChange(Color.TRANSPARENT, color)
    }

    private class StackPageTransformer : ViewPager.PageTransformer {

        override fun transformPage(view: View, position: Float) {
            var translationX = 0f
            if (position > 0.0f && position < 1.0f) {
                translationX = 0.8f * view.width * -position
            }

            view.translationX = translationX
        }
    }
}