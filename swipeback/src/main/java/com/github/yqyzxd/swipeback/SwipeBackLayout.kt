package com.github.yqyzxd.swipeback

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.customview.widget.ViewDragHelper
import kotlin.math.absoluteValue

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: SwipeBackLayout
 * Author: wind
 * Date: 2022/5/24 14:26
 * Description: 右滑返回
 * Path: 路径
 * History:
 *  <author> <time> <version> <desc>
 *
 */
internal class SwipeBackLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mAndroidContentLayoutChild: View? = null

    private var mSwipeListener: ISwipeListener = BuildInSwipeListener(context as Activity?)
    private val mDragHelper = ViewDragHelper.create(this, 1.0f, object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            if (mAndroidContentLayoutChild == null) {
                mAndroidContentLayoutChild = getChildAt(0)
            }
            return child == mAndroidContentLayoutChild
            //println("tryCaptureView $captured")

        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            onOnViewReleased(releasedChild, xvel, yvel)
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return this@SwipeBackLayout.width
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            //enable and disable transparent  activity
            if (left > 0) {
                mSwipeListener.onSwiping(left, SwipeState.START)
            }

            //println("clampViewPositionHorizontal left:$left  dx:$dx")
            return left.coerceAtLeast(0)
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            mAndroidContentLayoutChild?.apply {
                mYOffset = top
                mXOffset = left
            }

        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            val left = mAndroidContentLayoutChild?.left ?: 0
            //println("onViewDragStateChanged $state  left:$left")

            when (state) {
                ViewDragHelper.STATE_IDLE -> {
                    when (left) {
                        width -> {
                            //finish
                            mSwipeListener?.onSwiping(left, SwipeState.FINISH)
                        }

                        0 -> {
                            // disable tranparent activity
                            mSwipeListener?.onSwiping(left, SwipeState.RESET)
                        }
                    }
                }

                ViewDragHelper.STATE_DRAGGING, ViewDragHelper.STATE_SETTLING -> {

                }

            }

        }


    })


    private fun onOnViewReleased(releasedChild: View, xvel: Float, yvel: Float) {

        if (releasedChild.left >= width / 2 || (xvel.absoluteValue>yvel.absoluteValue && xvel >= FAST_VEL)) {
            mDragHelper.settleCapturedViewAt(width, top)
        } else {
            mDragHelper.settleCapturedViewAt(0, top)
        }
        invalidate()
    }

    init {
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT)
        setBackgroundColor(Color.TRANSPARENT)
    }

    private var mLastMotionX: Float = 0f
    private var mLastMotionY: Float = 0f

    private var mActivePointerId = INVALID_POINTER
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercept = false

        val action = ev.action and MotionEvent.ACTION_MASK
        when (action) {
            MotionEvent.ACTION_DOWN -> {

                mActivePointerId = ev.getPointerId(0)
                mLastMotionX = ev.x
                mLastMotionY = ev.y

                intercept = mDragHelper.shouldInterceptTouchEvent(ev)
                // println("ACTION_DOWN mDragHelper.shouldInterceptTouchEvent: $intercept")
            }

            MotionEvent.ACTION_MOVE -> {
                val activePointerId: Int = mActivePointerId
                if (activePointerId != INVALID_POINTER) {

                    val pointerIndex = ev.findPointerIndex(activePointerId)

                    val x = ev.getX(pointerIndex)
                    val dx = x - mLastMotionX
                    val y = ev.getY(pointerIndex)


                    val canScroll = canScroll(this, false, dx.toInt(), x.toInt(), y.toInt())
                    //println("ACTION_MOVE canScroll: $canScroll")
                    if (dx != 0f /*&& !isGutterDrag(mLastMotionX, dx)*/
                        && canScroll
                    ) {
                        // Nested view has scrollable area under this point. Let it be handled there.
                        mLastMotionX = x
                        mLastMotionY = y

                        intercept = false
                    } else {

                        intercept = mDragHelper.shouldInterceptTouchEvent(ev)
                        //println("ACTION_MOVE mDragHelper.shouldInterceptTouchEvent: $intercept")
                    }
                }


            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                intercept = false
            }

        }
        return intercept
    }

    /**
     * copy from ViewPager
     */
    private fun canScroll(v: View, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        if (v is ViewGroup) {
            val group = v
            val scrollX = v.getScrollX()
            val scrollY = v.getScrollY()
            val count = group.childCount
            // Count backwards - let topmost views consume scroll distance first.
            for (i in count - 1 downTo 0) {
                // TODO: Add versioned support here for transformed views.
                // This will not work for transformed views in Honeycomb+
                val child = group.getChildAt(i)
                if (x + scrollX >= child.left && x + scrollX < child.right && y + scrollY >= child.top && y + scrollY < child.bottom && canScroll(
                        child, true, dx, x + scrollX - child.left,
                        y + scrollY - child.top
                    )
                ) {
                    return true
                }
            }
        }
        return checkV && v.canScrollHorizontally(-dx)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mDragHelper.processTouchEvent(event)
        return true
    }


    override fun computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate()
        } else {
            mAndroidContentLayoutChild?.apply {
                mYOffset = top
                mXOffset = left
            }
        }
    }

    private var mXOffset: Int = 0
    private var mYOffset: Int = 0
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        //println("SwipeBackLayout onLayout")
        /**
         * 2.防止invalidate后拖动的view复位（跑回初始位置）
         */

        if (mXOffset != 0 || mYOffset != 0) {
            mAndroidContentLayoutChild?.apply {
                offsetLeftAndRight(mXOffset)
                offsetTopAndBottom(mYOffset)
            }

        }
    }

    companion object {
        const val FAST_VEL = 500
        const val INVALID_POINTER = -1
    }
}