package com.ashish.movieguide.ui.base.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.extensions.bindOptionalView
import com.ashish.movieguide.utils.extensions.getExtrasOrRestore
import com.ashish.movieguide.utils.extensions.performAction
import com.evernote.android.state.StateSaver

/**
 * Created by Ashish on Dec 26.
 */
abstract class BaseActivity : AppCompatActivity() {

    protected val toolbar: Toolbar? by bindOptionalView(R.id.toolbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        setSupportActionBar(toolbar)

        savedInstanceState.getExtrasOrRestore(this) {
            getIntentExtras(intent.extras)
        }
    }

    abstract fun getLayoutId(): Int

    protected open fun getIntentExtras(extras: Bundle?) {}

    @SuppressLint("NonMatchingStateSaverCalls")
    override fun onSaveInstanceState(outState: Bundle) {
        StateSaver.saveInstanceState(this, outState)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> performAction { finishAfterTransition() }
        else -> super.onOptionsItemSelected(item)
    }
}