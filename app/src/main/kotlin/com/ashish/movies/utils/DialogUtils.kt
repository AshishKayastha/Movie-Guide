package com.ashish.movies.utils

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.View
import com.ashish.movies.R
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.FontUtils.MONTSERRAT_MEDIUM
import com.ashish.movies.utils.FontUtils.MONTSERRAT_REGULAR
import com.ashish.movies.utils.FontUtils.getTypeface
import com.ashish.movies.utils.extensions.getTextWithCustomTypeface
import javax.inject.Inject

/**
 * Created by Ashish on Jan 30.
 */
class DialogUtils @Inject constructor(private val context: Context) {

    @SuppressLint("InflateParams")
    private val contentView: View = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)

    private val mediumTypefaceSpan: TypefaceSpan = CustomTypefaceSpan(getTypeface(context, MONTSERRAT_MEDIUM))
    private val regularTypefaceSpan: TypefaceSpan = CustomTypefaceSpan(getTypeface(context, MONTSERRAT_REGULAR))

    private val contentText: FontTextView
    private var messageDialog: AlertDialog? = null
    private var progressDialog: ProgressDialog? = null

    init {
        contentText = contentView.findViewById(R.id.loading_text) as FontTextView
    }

    @Suppress("unused")
    fun showOkMessageDialog(@StringRes contentId: Int) {
        showOkMessageDialog(getString(contentId))
    }

    fun showOkMessageDialog(content: String) {
        buildDialog().withContent(content)
                .withPositiveButton(getString(android.R.string.ok), null)
                .show()
    }

    fun buildDialog(): DialogUtils {
        if (messageDialog == null) {
            messageDialog = AlertDialog.Builder(context)
                    .setCancelable(false)
                    .create()
        }

        withTitle(null)
        withPositiveButton("", null)
        withNegativeButton("", null)
        return this
    }

    fun withTitle(@StringRes titleId: Int): DialogUtils {
        return withTitle(getString(titleId))
    }

    fun withTitle(title: CharSequence?): DialogUtils {
        checkMessageDialog()
        if (title.isNullOrEmpty()) {
            val titleText = title!!.getTextWithCustomTypeface(mediumTypefaceSpan)
            messageDialog?.setTitle(titleText)
        }

        return this
    }

    fun withContent(@StringRes contentId: Int): DialogUtils {
        return withContent(getString(contentId))
    }

    fun withContent(content: CharSequence): DialogUtils {
        checkMessageDialog()
        val contentText = content.getTextWithCustomTypeface(regularTypefaceSpan)
        messageDialog?.setMessage(contentText)
        return this
    }

    fun withPositiveButton(@StringRes positiveTextId: Int, buttonClick: (() -> Unit)? = null): DialogUtils {
        return withPositiveButton(getString(positiveTextId), buttonClick)
    }

    fun withPositiveButton(positiveText: CharSequence, buttonClick: (() -> Unit)? = null): DialogUtils {
        checkMessageDialog()

        val positiveBtnText = positiveText.getTextWithCustomTypeface(mediumTypefaceSpan)
        messageDialog?.setButton(DialogInterface.BUTTON_POSITIVE, positiveBtnText, { dialogInterface, which ->
            messageDialog?.dismiss()
            buttonClick?.invoke()
        })

        return this
    }

    fun withNegativeButton(negativeTextId: Int, buttonClick: (() -> Unit)? = null): DialogUtils {
        return withNegativeButton(getString(negativeTextId), buttonClick)
    }

    fun withNegativeButton(negativeText: CharSequence, buttonClick: (() -> Unit)? = null): DialogUtils {
        checkMessageDialog()

        val negativeBtnText = negativeText.getTextWithCustomTypeface(mediumTypefaceSpan)
        messageDialog?.setButton(DialogInterface.BUTTON_NEGATIVE, negativeBtnText, { dialogInterface, which ->
            messageDialog?.dismiss()
            buttonClick?.invoke()
        })

        return this
    }

    private fun getString(@StringRes stringId: Int): String {
        return if (stringId != 0) context.getString(stringId) else ""
    }

    fun show() {
        checkMessageDialog()
        messageDialog?.show()
    }

    private fun checkMessageDialog() {
        if (messageDialog == null) {
            throw NullPointerException("You should call buildDialog() method first!")
        }
    }

    fun dismissMessageDialog() = messageDialog?.dismiss()

    fun showProgressDialog(@StringRes contentId: Int) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context)
            progressDialog!!.setContentView(contentView)
            progressDialog!!.setCancelable(false)
        }

        contentText.setText(contentId)
        progressDialog!!.show()
    }

    fun dismissProgressDialog() = progressDialog?.dismiss()

    fun dismissAllDialogs() {
        dismissMessageDialog()
        dismissProgressDialog()
    }
}