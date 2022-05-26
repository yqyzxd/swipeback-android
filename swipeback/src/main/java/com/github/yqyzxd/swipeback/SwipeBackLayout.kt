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
class SwipeBackLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mAndroidContentLayoutChild: View? = null

    private var mSwipeListener:ISwipeListener = BuildInSwipeListener(context as Activity?)
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
            if(left>0){
                mSwipeListener.onSwiping(left,SwipeState.START)
            }

            //println("clampViewPositionHorizontal left:$left  dx:$dx")
            return left.coerceAtLeast(0)
        }



        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            val left=mAndroidContentLayoutChild?.left ?: 0
            //println("onViewDragStateChanged $state  left:$left")

            when(state){
                ViewDragHelper.STATE_IDLE -> {
                    when(left){
                        width->{
                            //finish
                            mSwipeListener?.onSwiping(left,SwipeState.FINISH)
                        }

                        0->{
                           // disable tranparent activity
                            mSwipeListener?.onSwiping(left,SwipeState.RESET)
                        }
                    }
                }

                ViewDragHelper.STATE_DRAGGING,ViewDragHelper.STATE_SETTLING->{

                }

            }

        }


    })


    private fun onOnViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
        if (releasedChild.left >= width / 2 || xvel >= FAST_VEL) {
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


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return  mDragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mDragHelper.processTouchEvent(event)
        return true
    }


    override fun computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate()
        }
    }

    companion object{
        const val FAST_VEL=500
    }
}