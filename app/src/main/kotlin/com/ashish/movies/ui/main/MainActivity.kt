package com.ashish.movies.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import com.ashish.movies.R
import com.ashish.movies.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            item.isChecked = true
            when (item.itemId) {
                R.id.action_movies -> replaceFragment(null)
                R.id.action_tv_shows -> replaceFragment(null)
                R.id.action_people -> replaceFragment(null)
            }
            false
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    fun replaceFragment(fragment: Fragment?) {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.container, fragment, "")
                .commit()
    }
}