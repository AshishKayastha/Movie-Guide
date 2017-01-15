package com.ashish.movies.ui.base.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import butterknife.bindOptionalView
import com.ashish.movies.R
import com.ashish.movies.app.MoviesApp
import com.ashish.movies.di.components.AppComponent
import icepick.Icepick

/**
 * Created by Ashish on Dec 26.
 */
abstract class BaseActivity : AppCompatActivity() {

    protected val toolbar: Toolbar? by bindOptionalView(R.id.toolbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(MoviesApp.getAppComponent(this))
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            getIntentExtras(intent.extras)
        } else {
            Icepick.restoreInstanceState(this, savedInstanceState)
        }
    }

    protected open fun getIntentExtras(extras: Bundle?) {}

    open fun injectDependencies(appComponent: AppComponent) {}

    abstract fun getLayoutId(): Int

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            supportFinishAfterTransition()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}