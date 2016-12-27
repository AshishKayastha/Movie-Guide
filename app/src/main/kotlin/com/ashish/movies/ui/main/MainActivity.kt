package com.ashish.movies.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import com.ashish.movies.R
import com.ashish.movies.ui.base.BaseActivity
import com.ashish.movies.ui.movies.MoviesFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            item.isChecked = true
            when (item.itemId) {
                R.id.action_movies -> replaceFragment(MoviesFragment.newInstance())
                R.id.action_tv_shows -> replaceFragment(MoviesFragment.newInstance())
                R.id.action_people -> replaceFragment(MoviesFragment.newInstance())
            }
            false
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.container, fragment, "")
                .commit()
    }
}