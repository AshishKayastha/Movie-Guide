package com.ashish.movieguide.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.text.style.TypefaceSpan
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.utils.FontUtils.MONTSERRAT_MEDIUM
import com.ashish.movieguide.utils.FontUtils.MONTSERRAT_REGULAR
import com.ashish.movieguide.utils.FontUtils.getTypeface
import com.ashish.movieguide.utils.extensions.changeDialogButtonTypeface
import com.ashish.movieguide.utils.extensions.find
import com.ashish.movieguide.utils.extensions.getTextWithCustomTypeface
import com.ashish.movieguide.utils.extensions.inflateLayout
import javax.inject.Inject

/**
 * Created by Ashish on Jan 30.
 */
@ActivityScope
class DialogUtils @Inject constructor(private val context: Context) {

    @SuppressLint("InflateParams")
    private val contentView: View = context.inflateLayout(R.layout.dialog_loading)

    private val mediumTypefaceSpan: TypefaceSpan = CustomTypefaceSpan(getTypeface(context, MONTSERRAT_MEDIUM))
    private val regularTypefaceSpan: TypefaceSpan = CustomTypefaceSpan(getTypeface(context, MONTSERRAT_REGULAR))

    private val contentText: FontTextView
    private var messageDialog: AlertDialog? = null
    private var progressDialog: AlertDialog? = null

    init {
        contentText = contentView.find<FontTextView>(R.id.loading_text)
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

            messageDialog?.changeDialogButtonTypeface()
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

        messageDialog?.setButton(DialogInterface.BUTTON_POSITIVE, positiveText, { _, _ ->
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

        messageDialog?.setButton(DialogInterface.BUTTON_NEGATIVE, negativeText, { _, _ ->
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