package com.ashish.movies.ui.multisearch

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageButton
import android.widget.TextView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.common.BaseActivity
import com.ashish.movies.utils.Utils
import com.ashish.movies.utils.extensions.*
import com.ashish.movies.utils.keyboardwatcher.KeyboardWatcher
import javax.inject.Inject

/**
 * Created by Ashish on Jan 05.
 */
class MultiSearchActivity : BaseActivity() {

    @Inject lateinit var keyboardWatcher: KeyboardWatcher

    private val rootView: View by bindView(R.id.content_layout)
    private val backIcon: ImageButton by bindView(R.id.back_icon)
    private val searchView: SearchView by bindView(R.id.search_view)

    private var endRadius: Float = 0f
    private var multiSearchFragment: MultiSearchFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            performRevealAnimation()

            multiSearchFragment = MultiSearchFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, multiSearchFragment)
                    .commit()
        }

        setupSearchView()
        backIcon.setOnClickListener { onBackPressed() }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(MultiSearchModule(this)).inject(this)
    }

    override fun getLayoutId() = R.layout.activity_multi_search

    private fun performRevealAnimation() {
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val right = rootView.right
                endRadius = Math.hypot(right.toDouble(), rootView.bottom.toDouble()).toFloat()
                rootView.startCircularRevealAnimation(right, 0, 0f, endRadius, 650L) {
                    searchView.showKeyboard()
                }
            }
        })
    }

    private fun setupSearchView() {
        val searchText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as TextView
        searchText.changeTypeface()
        searchText.setTextColor(Color.WHITE)
        searchText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        searchText.setHintTextColor(Color.parseColor("#D9E1E1E1"))

        searchView.maxWidth = Utils.getScreenWidth()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()
                multiSearchFragment?.searchQuery(query)
                return true
            }

            override fun onQueryTextChange(query: String) = true
        })
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        return keyboardWatcher.dispatchEditTextTouchEvent(event, super.dispatchTouchEvent(event))
    }

    override fun onBackPressed() {
        hideKeyboard()
        rootView.startCircularRevealAnimation(rootView.right, 0, endRadius, 0f, 500L) {
            rootView.hide()
            super.onBackPressed()
        }
    }

    override fun finishAfterTransition() {
        super.finishAfterTransition()
        overridePendingTransition(0, 0)
    }

    override fun onDestroy() {
        searchView.setOnQueryTextListener(null)
        super.onDestroy()
    }
}