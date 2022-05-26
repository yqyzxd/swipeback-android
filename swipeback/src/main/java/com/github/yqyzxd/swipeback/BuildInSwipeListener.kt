package com.github.yqyzxd.swipeback

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: BuildInSwipeListener
 * Author: wind
 * Date: 2022/5/25 16:44
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 *  <author> <time> <version> <desc>
 *
 */
class BuildInSwipeListener(private val mActivity: Activity?) : ISwipeListener {
    private var mStartCalled = false
    override fun onSwiping(left: Int, state: SwipeState) {

        when (state) {
            SwipeState.START -> {
                if (!mStartCalled) {
                    mStartCalled = true
                    // transparent activity
                    mActivity?.enableTranslucent(true)?.apply {
                        mActivity?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        //println("START enableTranslucent $this")
                    }
                }
            }
            SwipeState.SWIPING -> {
            }

            SwipeState.RESET -> {
                mStartCalled = false
                // disable transparent activity
                mActivity?.enableTranslucent(false)?.apply {
                    mActivity?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
                   // println("RESET disableTranslucent $this")
                }

            }
            SwipeState.FINISH -> {
                //finish activity
                mActivity?.apply {
                    if (isFinishing.not()) {
                        finish()
                        //println("FINISH")
                    }
                }


            }
        }
    }

}