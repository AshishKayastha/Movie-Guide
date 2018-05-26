package com.ashish.movieguide.ui.multisearch.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.base.common.BaseActivity
import com.ashish.movieguide.ui.multisearch.fragment.MultiSearchFragment
import com.ashish.movieguide.utils.Utils
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.changeTypeface
import com.ashish.movieguide.utils.extensions.find
import com.ashish.movieguide.utils.extensions.hide
import com.ashish.movieguide.utils.extensions.hideKeyboard
import com.ashish.movieguide.utils.extensions.onLayoutLaid
import com.ashish.movieguide.utils.extensions.showKeyboard
import com.ashish.movieguide.utils.extensions.startCircularRevealAnimation
import com.ashish.movieguide.utils.keyboardwatcher.KeyboardWatcher
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_multi_search.*
import javax.inject.Inject

/**
 * Created by Ashish on Jan 05.
 */
class MultiSearchActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject lateinit var keyboardWatcher: KeyboardWatcher
    @Inject lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    private val rootView: View by bindView(R.id.content_layout)

    private var endRadius: Float = 0f
    private var multiSearchFragment: MultiSearchFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
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

    override fun getLayoutId() = R.layout.activity_multi_search

    private fun performRevealAnimation() {
        rootView.onLayoutLaid {
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

    override fun supportFragmentInjector() = supportFragmentInjector

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