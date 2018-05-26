package com.ashish.movieguide.ui.widget

import android.content.Context
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.GridLayoutAnimationController
import com.ashish.movieguide.R

/**
 * Created by Ashish on Jan 18.
 */
class StaggeredGridRecyclerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : EmptyRecyclerView(context, attrs, defStyle) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (!isInEditMode) {
            layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation)
            startLayoutAnimation()
        }
    }

    override fun attachLayoutAnimationParameters(
            child: View,
            params: ViewGroup.LayoutParams,
            index: Int,
            count: Int
    ) {
        if (adapter != null && layoutManager is StaggeredGridLayoutManager) {
            var animParams: GridLayoutAnimationController.AnimationParameters? = params.layoutAnimationParameters as GridLayoutAnimationController.AnimationParameters?

            if (animParams == null) {
                animParams = GridLayoutAnimationController.AnimationParameters()
                params.layoutAnimationParameters = animParams
            }

            val columns = (layoutManager as StaggeredGridLayoutManager).spanCount
            animParams.count = count
            animParams.index = index
            animParams.columnsCount = columns
            animParams.rowsCount = count / columns

            val invertedIndex = count - 1 - index
            animParams.column = columns - 1 - invertedIndex % columns
            animParams.row = animParams.rowsCount - 1 - invertedIndex / columns
        } else {
            super.attachLayoutAnimationParameters(child, params, index, count)
        }
    }
}