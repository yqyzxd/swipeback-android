package com.github.yqyzxd.swipeback

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import com.github.yqyzxd.reflection.Reflection

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
    private var mWindowBackground: Drawable? = null
    private var mActivitytTranslucent:Boolean? = null
    override fun onSwiping(left: Int, state: SwipeState) {

        when (state) {
            SwipeState.START -> {
                if (!mStartCalled) {
                    mStartCalled = true

                    if (mActivitytTranslucent==null) {
                        mActivity?.apply {
                            mActivitytTranslucent = Reflection().on(window.javaClass)
                                .method("isTranslucent")
                                .invoke(window) ?: false
                        }
                    }
                    //println("mActivitytTranslucent :$mActivitytTranslucent")
                    mActivitytTranslucent?.apply {
                        if (this.not()){
                            mActivity?.enableTranslucent(true)?.apply {
                                if (mWindowBackground == null) {
                                    mWindowBackground = mActivity?.window?.decorView?.background
                                }
                                mActivity?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            }
                        }

                    }



                }
            }
            SwipeState.SWIPING -> {
            }

            SwipeState.RESET -> {
                mStartCalled = false

                mActivitytTranslucent?.apply {
                    if (this.not()){
                        mActivity?.enableTranslucent(false)?.apply {
                            mActivity?.window?.apply {
                                if (mWindowBackground != null) {
                                    setBackgroundDrawable(mWindowBackground)
                                } else {
                                    setBackgroundDrawable(ColorDrawable(Color.WHITE))
                                }
                            }
                        }
                    }

                }
            }
            SwipeState.FINISH -> {
                mActivity?.apply {
                    if (isFinishing.not()) {
                        finish()
                        overridePendingTransition(0, 0)
                    }
                }


            }
        }
    }

}