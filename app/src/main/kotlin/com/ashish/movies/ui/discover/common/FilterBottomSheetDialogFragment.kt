package com.ashish.movies.ui.discover.common

import android.app.Dialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import com.ashish.movies.R

/**
 * Created by Ashish on Jan 07.
 */
class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.bottom_sheet_filter_movies, null)
        dialog.setContentView(contentView)
    }
}