package com.ashish.movies.ui.main

import com.ashish.movies.ui.base.mvp.MvpView

/**
 * Created by Ashish on Jan 28.
 */
interface MainView : MvpView {

    fun showProgressDialog(messageId: Int)

    fun hideProgressDialog()

    fun validateRequestToken(tokenValidationUrl: String)

    fun onLoginSuccess()
}