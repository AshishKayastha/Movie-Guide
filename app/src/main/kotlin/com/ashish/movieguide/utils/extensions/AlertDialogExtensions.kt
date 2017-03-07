package com.ashish.movieguide.utils.extensions

import android.app.Dialog
import android.support.v7.app.AlertDialog

fun AlertDialog.changeDialogButtonTypeface() {
    setOnShowListener {
        getButton(Dialog.BUTTON_POSITIVE)?.changeTypeface()
        getButton(Dialog.BUTTON_NEGATIVE)?.changeTypeface()
        getButton(Dialog.BUTTON_NEUTRAL)?.changeTypeface()
    }
}

fun AlertDialog.enablePositiveButton(enable: Boolean) {
    getButton(Dialog.BUTTON_POSITIVE)?.isEnabled = enable
}

fun AlertDialog.enableNegativeButton(enable: Boolean) {
    getButton(Dialog.BUTTON_NEGATIVE)?.isEnabled = enable
}