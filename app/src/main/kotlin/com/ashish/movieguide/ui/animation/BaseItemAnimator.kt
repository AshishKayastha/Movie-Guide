package com.ashish.movieguide.ui.animation

import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.SimpleItemAnimator
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import java.util.ArrayList

abstract class BaseItemAnimator : SimpleItemAnimator() {

    companion object {

        @JvmStatic
        protected val INTERPOLATOR: Interpolator = LinearInterpolator()

        protected val addAnimations = ArrayList<ViewHolder>()
        protected val removeAnimations = ArrayList<ViewHolder>()

        private val pendingMoves = ArrayList<MoveInfo>()
        private val pendingChanges = ArrayList<ChangeInfo>()
        private val pendingRemovals = ArrayList<ViewHolder>()
        private val pendingAdditions = ArrayList<ViewHolder>()

        private val movesList = ArrayList<ArrayList<MoveInfo>>()
        private val changesList = ArrayList<ArrayList<ChangeInfo>>()
        private val additionsList = ArrayList<ArrayList<ViewHolder>>()

        private val moveAnimations = ArrayList<ViewHolder>()
        private val changeAnimations = ArrayList<ViewHolder>()

        private fun clear(view: View) {
            view.apply {
                alpha = 1f
                scaleX = 1f
                scaleY = 1f
                translationX = 0f
                translationY = 0f
                rotation = 0f
                rotationX = 0f
                rotationY = 0f
                pivotX = (measuredWidth / 2).toFloat()
                pivotY = (measuredHeight / 2).toFloat()
            }

            ViewCompat.animate(view).setInterpolator(null).startDelay = 0
        }
    }

    init {
        supportsChangeAnimations = false
    }

    override fun runPendingAnimations() {
        val removalsPending = !pendingRemovals.isEmpty()
        val movesPending = !pendingMoves.isEmpty()
        val changesPending = !pendingChanges.isEmpty()
        val additionsPending = !pendingAdditions.isEmpty()

        if (!removalsPending && !movesPending && !additionsPending && !changesPending) return

        // First, remove stuff
        for (holder in pendingRemovals) {
            animateRemoveImpl(holder)
        }
        pendingRemovals.clear()

        // Next, move stuff
        if (movesPending) {
            val moves = ArrayList<MoveInfo>()
            moves.addAll(pendingMoves)
            movesList.add(moves)
            pendingMoves.clear()
            val mover = Runnable {
                for (moveInfo in moves) {
                    animateMoveImpl(moveInfo.holder, moveInfo.fromX, moveInfo.fromY, moveInfo.toX, moveInfo.toY)
                }
                moves.clear()
                movesList.remove(moves)
            }

            if (removalsPending) {
                val view = moves[0].holder.itemView
                ViewCompat.postOnAnimationDelayed(view, mover, removeDuration)
            } else {
                mover.run()
            }
        }

        // Next, change stuff, to run in parallel with move animations
        if (changesPending) {
            val changes = ArrayList<ChangeInfo>()
            changes.addAll(pendingChanges)
            changesList.add(changes)
            pendingChanges.clear()
            val changer = Runnable {
                for (change in changes) {
                    animateChangeImpl(change)
                }
                changes.clear()
                changesList.remove(changes)
            }

            if (removalsPending) {
                val holder = changes[0].oldHolder
                ViewCompat.postOnAnimationDelayed(holder!!.itemView, changer, removeDuration)
            } else {
                changer.run()
            }
        }

        // Next, add stuff
        if (additionsPending) {
            val additions = ArrayList<ViewHolder>()
            additions.addAll(pendingAdditions)
            additionsList.add(additions)
            pendingAdditions.clear()
            val adder = Runnable {
                for (holder in additions) {
                    animateAddImpl(holder)
                }
                additions.clear()
                additionsList.remove(additions)
            }

            if (removalsPending || movesPending || changesPending) {
                val removeDuration = if (removalsPending) removeDuration else 0
                val moveDuration = if (movesPending) moveDuration else 0
                val changeDuration = if (changesPending) changeDuration else 0
                val totalDelay = removeDuration + Math.max(moveDuration, changeDuration)
                val view = additions[0].itemView
                ViewCompat.postOnAnimationDelayed(view, adder, totalDelay)
            } else {
                adder.run()
            }
        }
    }

    private fun preAnimateRemove(holder: ViewHolder) {
        clear(holder.itemView)
        preAnimateRemoveImpl(holder)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun preAnimateRemoveImpl(holder: ViewHolder) {
    }

    private fun animateRemoveImpl(holder: ViewHolder) {
        removeAnimation(holder)
        removeAnimations.add(holder)
    }

    protected abstract fun removeAnimation(holder: ViewHolder)

    override fun animateRemove(holder: ViewHolder): Boolean {
        endAnimation(holder)
        preAnimateRemove(holder)
        pendingRemovals.add(holder)
        return true
    }

    protected fun getRemoveDelay(holder: ViewHolder) = Math.abs(holder.oldPosition * removeDuration / 4)

    private fun preAnimateAdd(holder: ViewHolder) {
        clear(holder.itemView)
        preAnimateAddImpl(holder)
    }

    protected open fun preAnimateAddImpl(holder: ViewHolder) {}

    private fun animateAddImpl(holder: ViewHolder) {
        addAnimation(holder)
        addAnimations.add(holder)
    }

    protected abstract fun addAnimation(holder: ViewHolder)

    override fun animateAdd(holder: ViewHolder): Boolean {
        endAnimation(holder)
        preAnimateAdd(holder)
        pendingAdditions.add(holder)
        return true
    }

    protected fun getAddDelay(holder: ViewHolder) = Math.abs(holder.adapterPosition * addDuration / 4)

    override fun animateMove(holder: ViewHolder, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        var newFromX = fromX
        var newFromY = fromY
        val view = holder.itemView
        newFromX += holder.itemView.translationX.toInt()
        newFromY += holder.itemView.translationY.toInt()

        endAnimation(holder)
        val deltaX = toX - newFromX
        val deltaY = toY - newFromY
        if (deltaX == 0 && deltaY == 0) {
            dispatchMoveFinished(holder)
            return false
        }

        if (deltaX != 0) view.translationX = (-deltaX).toFloat()
        if (deltaY != 0) view.translationY = (-deltaY).toFloat()

        pendingMoves.add(MoveInfo(holder, newFromX, newFromY, toX, toY))
        return true
    }

    private fun animateMoveImpl(holder: ViewHolder, fromX: Int, fromY: Int, toX: Int, toY: Int) {
        val view = holder.itemView
        val deltaX = toX - fromX
        val deltaY = toY - fromY

        if (deltaX != 0) ViewCompat.animate(view).translationX(0f)
        if (deltaY != 0) ViewCompat.animate(view).translationY(0f)

        moveAnimations.add(holder)
        val animation = ViewCompat.animate(view)
        animation.setDuration(moveDuration).setListener(object : VpaListenerAdapter() {
            override fun onAnimationStart(view: View) = dispatchMoveStarting(holder)

            override fun onAnimationCancel(view: View) {
                if (deltaX != 0) view.translationX = 0f
                if (deltaY != 0) view.translationY = 0f
            }

            override fun onAnimationEnd(view: View) {
                animation.setListener(null)
                dispatchMoveFinished(holder)
                moveAnimations.remove(holder)
                dispatchFinishedWhenDone()
            }
        }).start()
    }

    override fun animateChange(oldHolder: ViewHolder, newHolder: ViewHolder?, fromX: Int, fromY: Int, toX: Int,
                               toY: Int): Boolean {
        val prevTranslationX = oldHolder.itemView.translationX
        val prevTranslationY = oldHolder.itemView.translationY
        val prevAlpha = oldHolder.itemView.alpha
        endAnimation(oldHolder)

        val deltaX = (toX.toFloat() - fromX.toFloat() - prevTranslationX).toInt()
        val deltaY = (toY.toFloat() - fromY.toFloat() - prevTranslationY).toInt()

        // recover prev translation state after ending animation
        oldHolder.itemView.apply {
            translationX = prevTranslationX
            translationY = prevTranslationY
            alpha = prevAlpha
        }

        if (newHolder?.itemView != null) {
            // carry over translation values
            endAnimation(newHolder)
            newHolder.itemView.apply {
                translationX = (-deltaX).toFloat()
                translationY = (-deltaY).toFloat()
                alpha = 0f
            }
        }

        pendingChanges.add(ChangeInfo(oldHolder, newHolder, fromX, fromY, toX, toY))
        return true
    }

    private fun animateChangeImpl(changeInfo: ChangeInfo) {
        val holder = changeInfo.oldHolder
        val view = holder?.itemView
        val newHolder = changeInfo.newHolder
        val newView = newHolder?.itemView

        if (view != null) {
            changeAnimations.add(changeInfo.oldHolder!!)
            val oldViewAnim = ViewCompat.animate(view).setDuration(changeDuration)
            oldViewAnim.translationX((changeInfo.toX - changeInfo.fromX).toFloat())
            oldViewAnim.translationY((changeInfo.toY - changeInfo.fromY).toFloat())

            oldViewAnim.alpha(0f).setListener(object : VpaListenerAdapter() {
                override fun onAnimationStart(view: View) = dispatchChangeStarting(changeInfo.oldHolder, true)

                override fun onAnimationEnd(view: View) {
                    oldViewAnim.setListener(null)
                    view.apply {
                        alpha = 1f
                        translationX = 0f
                        translationY = 0f
                    }
                    dispatchChangeFinished(changeInfo.oldHolder, true)
                    changeAnimations.remove(changeInfo.oldHolder!!)
                    dispatchFinishedWhenDone()
                }
            }).start()
        }

        if (newView != null) {
            changeAnimations.add(changeInfo.newHolder!!)
            val newViewAnimation = ViewCompat.animate(newView)

            newViewAnimation.alpha(1f)
                    .translationX(0f)
                    .translationY(0f)
                    .setDuration(changeDuration)
                    .setListener(object : VpaListenerAdapter() {
                        override fun onAnimationStart(view: View) {
                            dispatchChangeStarting(changeInfo.newHolder, false)
                        }

                        override fun onAnimationEnd(view: View) {
                            newViewAnimation.setListener(null)
                            newView.apply {
                                alpha = 1f
                                translationX = 0f
                                translationY = 0f
                            }
                            dispatchChangeFinished(changeInfo.newHolder, false)
                            changeAnimations.remove(changeInfo.newHolder!!)
                            dispatchFinishedWhenDone()
                        }
                    }).start()
        }
    }

    private fun endChangeAnimation(infoList: MutableList<ChangeInfo>, item: ViewHolder) {
        infoList.indices.reversed()
                .map { infoList[it] }
                .filter { endChangeAnimationIfNecessary(it, item) && it.oldHolder == null && it.newHolder == null }
                .forEach { infoList.remove(it) }
    }

    private fun endChangeAnimationIfNecessary(changeInfo: ChangeInfo) {
        changeInfo.oldHolder?.let { endChangeAnimationIfNecessary(changeInfo, it) }
        changeInfo.newHolder?.let { endChangeAnimationIfNecessary(changeInfo, it) }
    }

    private fun endChangeAnimationIfNecessary(changeInfo: ChangeInfo, item: ViewHolder): Boolean {
        var oldItem = false
        when (item) {
            changeInfo.newHolder -> changeInfo.newHolder = null
            changeInfo.oldHolder -> {
                changeInfo.oldHolder = null
                oldItem = true
            }
            else -> return false
        }

        item.itemView.apply {
            alpha = 1f
            translationX = 0f
            translationY = 0f
        }
        dispatchChangeFinished(item, oldItem)
        return true
    }

    override fun endAnimation(item: ViewHolder) {
        val view = item.itemView

        // this will trigger end callback which should set properties to their target values.
        ViewCompat.animate(view).cancel()
        for (i in pendingMoves.indices.reversed()) {
            val moveInfo = pendingMoves[i]
            if (moveInfo.holder === item) {
                view.apply {
                    translationX = 0f
                    translationY = 0f
                }
                dispatchMoveFinished(item)
                pendingMoves.removeAt(i)
            }
        }

        endChangeAnimation(pendingChanges, item)
        if (pendingRemovals.remove(item)) {
            clear(item.itemView)
            dispatchRemoveFinished(item)
        }

        if (pendingAdditions.remove(item)) {
            clear(item.itemView)
            dispatchAddFinished(item)
        }

        for (i in changesList.indices.reversed()) {
            val changes = changesList[i]
            endChangeAnimation(changes, item)
            if (changes.isEmpty()) {
                changesList.removeAt(i)
            }
        }

        for (i in movesList.indices.reversed()) {
            val moves = movesList[i]
            for (j in moves.indices.reversed()) {
                val moveInfo = moves[j]
                if (moveInfo.holder === item) {
                    view.apply {
                        translationX = 0f
                        translationY = 0f
                    }
                    dispatchMoveFinished(item)
                    moves.removeAt(j)
                    if (moves.isEmpty()) {
                        movesList.removeAt(i)
                    }
                    break
                }
            }
        }

        for (i in additionsList.indices.reversed()) {
            val additions = additionsList[i]
            if (additions.remove(item)) {
                clear(item.itemView)
                dispatchAddFinished(item)
                if (additions.isEmpty()) {
                    additionsList.removeAt(i)
                }
            }
        }

        dispatchFinishedWhenDone()
    }

    override fun isRunning(): Boolean {
        return !pendingAdditions.isEmpty() ||
                !pendingChanges.isEmpty() ||
                !pendingMoves.isEmpty() ||
                !pendingRemovals.isEmpty() ||
                !moveAnimations.isEmpty() ||
                !removeAnimations.isEmpty() ||
                !addAnimations.isEmpty() ||
                !changeAnimations.isEmpty() ||
                !movesList.isEmpty() ||
                !additionsList.isEmpty() ||
                !changesList.isEmpty()
    }

    /**
     * Check the state of currently pending and running animations. If there are none
     * pending/running, call #dispatchAnimationsFinished() to notify any listeners.
     */
    private fun dispatchFinishedWhenDone() {
        if (!isRunning) dispatchAnimationsFinished()
    }

    override fun endAnimations() {
        var count = pendingMoves.size
        for (i in count - 1 downTo 0) {
            val item = pendingMoves[i]
            val view = item.holder.itemView
            view.apply {
                translationX = 0f
                translationY = 0f
            }
            dispatchMoveFinished(item.holder)
            pendingMoves.removeAt(i)
        }

        count = pendingRemovals.size
        for (i in count - 1 downTo 0) {
            val item = pendingRemovals[i]
            dispatchRemoveFinished(item)
            pendingRemovals.removeAt(i)
        }

        count = pendingAdditions.size
        for (i in count - 1 downTo 0) {
            val item = pendingAdditions[i]
            clear(item.itemView)
            dispatchAddFinished(item)
            pendingAdditions.removeAt(i)
        }

        count = pendingChanges.size
        for (i in count - 1 downTo 0) {
            endChangeAnimationIfNecessary(pendingChanges[i])
        }

        pendingChanges.clear()
        if (!isRunning) return

        var listCount = movesList.size
        for (i in listCount - 1 downTo 0) {
            val moves = movesList[i]
            count = moves.size
            for (j in count - 1 downTo 0) {
                val moveInfo = moves[j]
                val item = moveInfo.holder
                val view = item.itemView
                view.apply {
                    translationX = 0f
                    translationY = 0f
                }
                dispatchMoveFinished(moveInfo.holder)
                moves.removeAt(j)
                if (moves.isEmpty()) {
                    movesList.remove(moves)
                }
            }
        }

        listCount = additionsList.size
        for (i in listCount - 1 downTo 0) {
            val additions = additionsList[i]
            count = additions.size
            for (j in count - 1 downTo 0) {
                val item = additions[j]
                val view = item.itemView
                view.alpha = 1f
                dispatchAddFinished(item)

                //this check prevent exception when removal already happened during finishing animation
                if (j < additions.size) {
                    additions.removeAt(j)
                }

                if (additions.isEmpty()) {
                    additionsList.remove(additions)
                }
            }
        }

        listCount = changesList.size
        for (i in listCount - 1 downTo 0) {
            val changes = changesList[i]
            count = changes.size
            for (j in count - 1 downTo 0) {
                endChangeAnimationIfNecessary(changes[j])
                if (changes.isEmpty()) {
                    changesList.remove(changes)
                }
            }
        }

        cancelAll(removeAnimations)
        cancelAll(moveAnimations)
        cancelAll(addAnimations)
        cancelAll(changeAnimations)

        dispatchAnimationsFinished()
    }

    private fun cancelAll(viewHolders: List<ViewHolder>) {
        for (i in viewHolders.indices.reversed()) {
            ViewCompat.animate(viewHolders[i].itemView).cancel()
        }
    }

    private class MoveInfo constructor(
            var holder: ViewHolder,
            var fromX: Int,
            var fromY: Int,
            var toX: Int,
            var toY: Int
    )

    private class ChangeInfo constructor(
            var oldHolder: ViewHolder?,
            var newHolder: ViewHolder?,
            val fromX: Int,
            val fromY: Int,
            val toX: Int,
            val toY: Int
    ) {

        override fun toString() = "ChangeInfo{" +
                "oldHolder=" + oldHolder +
                ", newHolder=" + newHolder +
                ", fromX=" + fromX +
                ", fromY=" + fromY +
                ", toX=" + toX +
                ", toY=" + toY +
                '}'
    }

    open class VpaListenerAdapter : ViewPropertyAnimatorListener {
        override fun onAnimationStart(view: View) {}

        override fun onAnimationEnd(view: View) {}

        override fun onAnimationCancel(view: View) {}
    }

    protected inner class DefaultAddVpaListener(private var viewHolder: ViewHolder) : VpaListenerAdapter() {

        override fun onAnimationStart(view: View) = dispatchAddStarting(viewHolder)

        override fun onAnimationCancel(view: View) = clear(view)

        override fun onAnimationEnd(view: View) {
            clear(view)
            dispatchAddFinished(viewHolder)
            addAnimations.remove(viewHolder)
            dispatchFinishedWhenDone()
        }
    }

    protected inner class DefaultRemoveVpaListener(private var viewHolder: ViewHolder) : VpaListenerAdapter() {

        override fun onAnimationStart(view: View) = dispatchRemoveStarting(viewHolder)

        override fun onAnimationCancel(view: View) = clear(view)

        override fun onAnimationEnd(view: View) {
            clear(view)
            dispatchRemoveFinished(viewHolder)
            removeAnimations.remove(viewHolder)
            dispatchFinishedWhenDone()
        }
    }
}