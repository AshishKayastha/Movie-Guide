package com.ashish.movies.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View

class GravitySnapHelper @JvmOverloads constructor(private val gravity: Int,
                                                  private var isSnapLastItemEnabled: Boolean = false) : LinearSnapHelper() {

    private var verticalHelper: OrientationHelper? = null
    private var horizontalHelper: OrientationHelper? = null

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? {
        val out = IntArray(2)
        if (layoutManager.canScrollHorizontally()) {
            val helper = getHorizontalHelper(layoutManager)
            out[0] = getDistance(gravity == Gravity.START, targetView, helper)
        } else {
            out[0] = 0
        }

        if (layoutManager.canScrollVertically()) {
            val helper = getVerticalHelper(layoutManager)
            out[1] = getDistance(gravity == Gravity.TOP, targetView, helper)
        } else {
            out[1] = 0
        }

        return out
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        var snapView: View? = null
        if (layoutManager is LinearLayoutManager) {
            val helper = getHorizontalHelper(layoutManager)
            when (gravity) {
                Gravity.START -> snapView = findStartView(layoutManager, helper)
                Gravity.END -> snapView = findEndView(layoutManager, helper)
                Gravity.TOP -> snapView = findStartView(layoutManager, helper)
                Gravity.BOTTOM -> snapView = findEndView(layoutManager, helper)
            }
        }

        return snapView
    }

    /**
     * Enable snapping of the last item that's snappable.
     * The default value is false, because you can't see the last item completely
     * if this is enabled.
     *
     * @param snap true if you want to enable snapping of the last snappable item
     */
    fun enableLastItemSnap(snap: Boolean) {
        isSnapLastItemEnabled = snap
    }

    private fun getDistance(isStart: Boolean, targetView: View, helper: OrientationHelper): Int {
        if (isStart) {
            return helper.getDecoratedStart(targetView) - helper.startAfterPadding
        } else {
            return helper.getDecoratedEnd(targetView) - helper.endAfterPadding
        }
    }

    /**
     * Returns the first view that we should snap to.
     *
     * @param layoutManager the recyclerview's layout manager
     * @param helper        orientation helper to calculate view sizes
     * @return the first view in the LayoutManager to snap to
     */
    private fun findStartView(layoutManager: RecyclerView.LayoutManager, helper: OrientationHelper): View? {
        if (layoutManager is LinearLayoutManager) {
            val firstChild = layoutManager.findFirstVisibleItemPosition()
            if (firstChild == RecyclerView.NO_POSITION) return null

            val child = layoutManager.findViewByPosition(firstChild)

            // We should return the child if it's visible width
            // is greater than 0.5 of it's total width.
            val visibleWidth = helper.getDecoratedEnd(child).toFloat() / helper.getDecoratedMeasurement(child)

            // If we're at the end of the list, we shouldn't snap
            // to avoid having the last item not completely visible.
            val endOfList = layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1

            if (visibleWidth > 0.5f && !endOfList) {
                return child
            } else if (isSnapLastItemEnabled && endOfList) {
                return child
            } else if (endOfList) {
                return null
            } else {
                // If the child wasn't returned, we need to return
                // the next view close to the start.
                return layoutManager.findViewByPosition(firstChild + 1)
            }
        }

        return null
    }

    private fun findEndView(layoutManager: RecyclerView.LayoutManager, helper: OrientationHelper): View? {
        if (layoutManager is LinearLayoutManager) {
            val lastChild = layoutManager.findLastVisibleItemPosition()
            if (lastChild == RecyclerView.NO_POSITION) return null

            val child = layoutManager.findViewByPosition(lastChild)
            val visibleWidth = (helper.totalSpace - helper.getDecoratedStart(child)).toFloat() / helper.getDecoratedMeasurement(child)

            // If we're at the start of the list, we shouldn't snap
            // to avoid having the first item not completely visible.
            val startOfList = layoutManager.findFirstCompletelyVisibleItemPosition() == 0

            if (visibleWidth > 0.5f && !startOfList) {
                return child
            } else if (isSnapLastItemEnabled && startOfList) {
                return child
            } else if (startOfList) {
                return null
            } else {
                // If the child wasn't returned, we need to return the previous view
                return layoutManager.findViewByPosition(lastChild - 1)
            }
        }
        return null
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        return verticalHelper ?: OrientationHelper.createVerticalHelper(layoutManager)
    }

    private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        return horizontalHelper ?: OrientationHelper.createHorizontalHelper(layoutManager)
    }
}