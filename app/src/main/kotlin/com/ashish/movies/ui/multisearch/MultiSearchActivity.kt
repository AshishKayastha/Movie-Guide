package com.ashish.movies.ui.multisearch

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.TextView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.common.BaseActivity
import com.ashish.movies.utils.FontUtils
import com.ashish.movies.utils.Utils
import com.ashish.movies.utils.extensions.hideKeyboard
import com.ashish.movies.utils.extensions.showKeyboard
import com.ashish.movies.utils.keyboardwatcher.KeyboardWatcher
import javax.inject.Inject

/**
 * Created by Ashish on Jan 05.
 */
class MultiSearchActivity : BaseActivity() {

    @Inject lateinit var keyboardWatcher: KeyboardWatcher

    private val backIcon: ImageButton by bindView(R.id.back_icon)
    private val searchView: SearchView by bindView(R.id.search_view)

    private var multiSearchFragment: MultiSearchFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            multiSearchFragment = MultiSearchFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, multiSearchFragment)
                    .commit()
        }

        setupSearchView()
        searchView.showKeyboard()
        backIcon.setOnClickListener { finish() }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(MultiSearchModule(this)).inject(this)
    }

    override fun getLayoutId() = R.layout.activity_multi_search

    private fun setupSearchView() {
        val searchText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as TextView
        searchText.setTextColor(Color.WHITE)
        searchText.setHintTextColor(Color.parseColor("#D9E1E1E1"))
        searchText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        FontUtils.setFontStyle(searchText, FontUtils.MONTSERRAT_REGULAR)

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

    override fun onDestroy() {
        searchView.setOnQueryTextListener(null)
        super.onDestroy()
    }
}