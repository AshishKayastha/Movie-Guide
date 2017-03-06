package com.ashish.movieguide.ui.base.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.ashish.movieguide.R
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movieguide.utils.extensions.bindOptionalView
import com.ashish.movieguide.utils.extensions.getExtrasOrRestore
import icepick.Icepick

/**
 * Created by Ashish on Dec 26.
 */
abstract class BaseActivity : AppCompatActivity() {

    protected val toolbar: Toolbar? by bindOptionalView(R.id.toolbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(application as ActivityComponentBuilderHost)
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        setSupportActionBar(toolbar)

        savedInstanceState.getExtrasOrRestore(this) {
            getIntentExtras(intent.extras)
        }
    }

    protected open fun getIntentExtras(extras: Bundle?) {}

    protected open fun injectDependencies(builderHost: ActivityComponentBuilderHost) {}

    abstract fun getLayoutId(): Int

    override fun onSaveInstanceState(outState: Bundle?) {
        Icepick.saveInstanceState(this, outState)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> performAction { finishAfterTransition() }
        else -> super.onOptionsItemSelected(item)
    }

    inline fun performAction(action: () -> Unit): Boolean {
        action()
        return true
    }
}