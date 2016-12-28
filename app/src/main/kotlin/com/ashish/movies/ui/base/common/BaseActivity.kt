package com.ashish.movies.ui.base.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by Ashish on Dec 26.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
    }

    abstract fun getLayoutId(): Int
}