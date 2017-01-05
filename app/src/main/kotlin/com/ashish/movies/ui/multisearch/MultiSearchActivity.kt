package com.ashish.movies.ui.multisearch

import android.os.Bundle
import com.ashish.movies.R
import com.ashish.movies.ui.base.common.BaseActivity

/**
 * Created by Ashish on Jan 05.
 */
class MultiSearchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MultiSearchFragment.newInstance())
                    .commit()
        }
    }

    override fun getLayoutId() = R.layout.activity_multi_search
}