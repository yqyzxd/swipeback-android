package com.github.yqyzxd.swipeback

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: BuildInSwipeListener
 * Author: wind
 * Date: 2022/5/25 16:44
 * Description: 根据swipelayout滑动状态做出相应动作
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
                    mActivity?.enableTranslucent(true)?.apply {
                        mActivity?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    }
                }
            }
            SwipeState.SWIPING -> {
            }

            SwipeState.RESET -> {
                mStartCalled = false
                mActivity?.enableTranslucent(false)?.apply {
                    mActivity?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
                }

            }
            SwipeState.FINISH -> {
                mActivity?.apply {
                    if (isFinishing.not()) {
                        finish()
                        overridePendingTransition(0,0)
                    }
                }


            }
        }
    }

}