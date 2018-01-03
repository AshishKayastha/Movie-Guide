package com.ashish.movieguide.ui.animation

import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView

class SlideInUpAnimator : BaseItemAnimator() {

    override fun removeAnimation(holder: RecyclerView.ViewHolder) {
        ViewCompat.animate(holder.itemView)
                .translationY(holder.itemView.height.toFloat())
                .alpha(0f)
                .setDuration(removeDuration)
                .setInterpolator(BaseItemAnimator.INTERPOLATOR)
                .setListener(DefaultRemoveVpaListener(holder))
                .setStartDelay(getRemoveDelay(holder))
                .start()
    }

    override fun preAnimateAddImpl(holder: RecyclerView.ViewHolder) {
        holder.itemView.apply {
            alpha = 0f
            translationY = height.toFloat()
        }
    }

    override fun addAnimation(holder: RecyclerView.ViewHolder) {
        ViewCompat.animate(holder.itemView)
                .translationY(0f)
                .alpha(1f)
                .setDuration(addDuration)
                .setInterpolator(BaseItemAnimator.INTERPOLATOR)
                .setListener(DefaultAddVpaListener(holder))
                .setStartDelay(getAddDelay(holder))
                .start()
    }
}