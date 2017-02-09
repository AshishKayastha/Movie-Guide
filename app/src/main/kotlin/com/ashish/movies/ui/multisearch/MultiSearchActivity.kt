package com.ashish.movies.ui.multisearch

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.di.components.UiComponent
import com.ashish.movies.ui.base.common.BaseActivity
import com.ashish.movies.utils.Utils
import com.ashish.movies.utils.extensions.afterMeasured
import com.ashish.movies.utils.extensions.changeTypeface
import com.ashish.movies.utils.extensions.find
import com.ashish.movies.utils.extensions.hide
import com.ashish.movies.utils.extensions.hideKeyboard
import com.ashish.movies.utils.extensions.showKeyboard
import com.ashish.movies.utils.extensions.startCircularRevealAnimation
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
        handleVoiceSearchIntent(intent)
        backIcon.setOnClickListener { onBackPressed() }
    }

    override fun injectDependencies(uiComponent: UiComponent) = uiComponent.inject(this)

    override fun getLayoutId() = R.layout.activity_multi_search

    private fun performRevealAnimation() {
        rootView.afterMeasured {
            val right = rootView.right
            endRadius = Math.hypot(right.toDouble(), rootView.bottom.toDouble()).toFloat()
            rootView.startCircularRevealAnimation(right, 0, 0f, endRadius, 650L) {
                searchView.showKeyboard()
            }
        }
    }

    private fun setupSearchView() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        val searchText = searchView.find<TextView>(android.support.v7.appcompat.R.id.search_src_text)
        searchText.apply {
            changeTypeface()
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setHintTextColor(Color.parseColor("#D9E1E1E1"))
        }

        searchView.maxWidth = Utils.getScreenWidth()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(query: String) = true
        })
    }

    private fun performSearch(query: String) {
        hideKeyboard()
        searchView.clearFocus()
        multiSearchFragment?.searchQuery(query)
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleVoiceSearchIntent(intent)
    }

    private fun handleVoiceSearchIntent(intent: Intent?) {
        if (intent != null && Intent.ACTION_SEARCH == intent.action) {
            performSearch(intent.getStringExtra(SearchManager.QUERY))
        }
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