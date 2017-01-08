package com.ashish.movies.ui.multisearch

import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.common.BaseActivity
import com.ashish.movies.ui.widget.FontEditText
import com.ashish.movies.utils.extensions.hideKeyboard
import com.ashish.movies.utils.extensions.showKeyboard
import com.ashish.movies.utils.keyboardwatcher.KeyboardWatcher
import javax.inject.Inject

/**
 * Created by Ashish on Jan 05.
 */
class MultiSearchActivity : BaseActivity() {

    @Inject lateinit var keyboardWatcher: KeyboardWatcher

    private val backIcon: ImageView by bindView(R.id.back_icon)
    private val searchEditText: FontEditText by bindView(R.id.search_edit_text)

    private var multiSearchFragment: MultiSearchFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            multiSearchFragment = MultiSearchFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, multiSearchFragment)
                    .commit()
        }

        searchEditText.showKeyboard()
        backIcon.setOnClickListener { finish() }

        searchEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                multiSearchFragment?.searchQuery(textView.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(MultiSearchModule(this)).inject(this)
    }

    override fun getLayoutId() = R.layout.activity_multi_search

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        return keyboardWatcher.dispatchEditTextTouchEvent(event, super.dispatchTouchEvent(event))
    }

    override fun onDestroy() {
        searchEditText.setOnEditorActionListener(null)
        super.onDestroy()
    }
}