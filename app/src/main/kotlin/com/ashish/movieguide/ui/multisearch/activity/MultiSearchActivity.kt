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
import android.view.ViewTreeObserver
import android.widget.ImageButton
import android.widget.TextView
import butterknife.bindView
import com.ashish.movieguide.R
import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.base.common.BaseActivity
import com.ashish.movieguide.ui.multisearch.fragment.MultiSearchFragment
import com.ashish.movieguide.utils.Utils
import com.ashish.movieguide.utils.extensions.changeTypeface
import com.ashish.movieguide.utils.extensions.find
import com.ashish.movieguide.utils.extensions.hide
import com.ashish.movieguide.utils.extensions.hideKeyboard
import com.ashish.movieguide.utils.extensions.showKeyboard
import com.ashish.movieguide.utils.extensions.startCircularRevealAnimation
import com.ashish.movieguide.utils.keyboardwatcher.KeyboardWatcher
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Ashish on Jan 05.
 */
class MultiSearchActivity : BaseActivity(), FragmentComponentBuilderHost {

    @Inject lateinit var keyboardWatcher: KeyboardWatcher

    @Inject
    lateinit var componentBuilders: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<FragmentComponentBuilder<*, *>>>

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

    override fun injectDependencies(builderHost: ActivityComponentBuilderHost) {
        builderHost.getActivityComponentBuilder(MultiSearchActivity::class.java, MultiSearchComponent.Builder::class.java)
                .withModule(ActivityModule(this))
                .build()
                .inject(this)
    }

    override fun <F : Fragment, B : FragmentComponentBuilder<F, AbstractComponent<F>>>
            getFragmentComponentBuilder(fragmentKey: Class<F>, builderType: Class<B>): B {
        return builderType.cast(componentBuilders[fragmentKey]!!.get())
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