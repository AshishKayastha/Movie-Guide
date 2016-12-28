package com.ashish.movies.ui.main

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import com.ashish.movies.R
import com.ashish.movies.ui.base.common.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : BaseActivity() {

    private var tabPagerAdapter: TabPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        tabPagerAdapter = TabPagerAdapter(supportFragmentManager)
        viewPager.adapter = tabPagerAdapter

        navigationView.setNavigationItemSelectedListener { item ->
            item.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun getLayoutId() = R.layout.activity_main

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}