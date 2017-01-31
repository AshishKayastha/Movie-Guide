package com.ashish.movies.utils

import android.annotation.SuppressLint
import android.app.Dialog
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
import com.ashish.movies.utils.extensions.changeTypeface
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
    private var progressDialog: AlertDialog? = null

    init {
        contentText = contentView.findViewById(R.id.loading_text) as FontTextView
    }

    @Suppress("unused")
    fun showOkMessageDialog(@StringRes contentId: Int) {
        showOkMessageDialog(getString(contentId))
    }

    fun showOkMessageDialog(content: CharSequence?) {
        buildDialog().withContent(content)
                .withPositiveButton(getString(android.R.string.ok))
                .show()
    }

    fun buildDialog(): DialogUtils {
        if (messageDialog == null) {
            messageDialog = AlertDialog.Builder(context, R.style.AlertDialogTheme)
                    .setCancelable(false)
                    .create()

            messageDialog?.setOnShowListener {
                val btnPositive = messageDialog?.getButton(Dialog.BUTTON_POSITIVE)
                btnPositive?.changeTypeface()

                val btnNegative = messageDialog?.getButton(Dialog.BUTTON_NEGATIVE)
                btnNegative?.changeTypeface()
            }
        }

        withTitle(null)
        withPositiveButton(null)
        withNegativeButton(null)
        return this
    }

    fun withTitle(@StringRes titleId: Int) = withTitle(getString(titleId))

    fun withTitle(title: CharSequence?): DialogUtils {
        checkMessageDialog()
        messageDialog?.setTitle(title.getTextWithCustomTypeface(mediumTypefaceSpan))
        return this
    }

    fun withContent(@StringRes contentId: Int) = withContent(getString(contentId))

    fun withContent(content: CharSequence?): DialogUtils {
        checkMessageDialog()
        messageDialog?.setMessage(content.getTextWithCustomTypeface(regularTypefaceSpan))
        return this
    }

    fun withPositiveButton(@StringRes positiveTextId: Int, positiveBtnClick: (() -> Unit)? = null): DialogUtils {
        return withPositiveButton(getString(positiveTextId), positiveBtnClick)
    }

    fun withPositiveButton(positiveText: CharSequence?, positiveBtnClick: (() -> Unit)? = null): DialogUtils {
        checkMessageDialog()

        messageDialog?.setButton(DialogInterface.BUTTON_POSITIVE, positiveText, { dialogInterface, which ->
            messageDialog?.dismiss()
            positiveBtnClick?.invoke()
        })

        return this
    }

    fun withNegativeButton(@StringRes negativeTextId: Int, negativeBtnClick: (() -> Unit)? = null): DialogUtils {
        return withNegativeButton(getString(negativeTextId), negativeBtnClick)
    }

    fun withNegativeButton(negativeText: CharSequence?, negativeBtnClick: (() -> Unit)? = null): DialogUtils {
        checkMessageDialog()

        messageDialog?.setButton(DialogInterface.BUTTON_NEGATIVE, negativeText, { dialogInterface, which ->
            messageDialog?.dismiss()
            negativeBtnClick?.invoke()
        })

        return this
    }

    private fun getString(@StringRes stringId: Int) = if (stringId != 0) context.getString(stringId) else null

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
            progressDialog = AlertDialog.Builder(context, R.style.AlertDialogTheme)
                    .setView(contentView)
                    .setCancelable(false)
                    .create()
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