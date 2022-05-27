package com.github.yqyzxd.swipeback

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: SwipeBacks
 * Author: wind
 * Date: 2022/5/24 14:49
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 *  <author> <time> <version> <desc>
 *
 */
class SwipeBacks {

    companion object {
        @JvmOverloads
        @JvmStatic
        fun install(app: Application,callback: OnSwipeBackFilterCallback?=null) {

            val callbacks= mutableListOf<OnSwipeBackFilterCallback>(DefaultSwipeBackFilterCallback())
            callback?.apply {
                callbacks.add(callback)
            }
            app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                    //println("${activity.javaClass.simpleName}ActivityLifecycleCallbacks onActivityCreated")

                }

                override fun onActivityPostCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?
                ) {
                    super.onActivityPostCreated(activity, savedInstanceState)

                    var filter=false
                    callbacks.forEach { cb->
                        filter=cb.onFilter(activity)
                    }

                    activity.javaClass.getAnnotation(DisableSwipeBack::class.java)?.apply {
                        filter=true
                    }
                    //println("${activity.javaClass.canonicalName} ${filter}")
                    if (filter) return


                    /**
                     *  <intent-filter>
                            <action android:name="android.intent.action.MAIN" />
                            <category android:name="android.intent.category.LAUNCHER" />
                        </intent-filter>
                     */

                    val decorView: ViewGroup = activity.window.decorView as ViewGroup
                    val androidContentLayout =
                        decorView.findViewById<ViewGroup>(android.R.id.content)

                    val contentLayoutChild = androidContentLayout.getChildAt(0)
                    androidContentLayout.removeView(contentLayoutChild)

                    val swipeBackLayout = SwipeBackLayout(activity)
                    swipeBackLayout.addView(contentLayoutChild)
                    androidContentLayout.addView(swipeBackLayout)

                }

                override fun onActivityStarted(activity: Activity) {
                }

                override fun onActivityResumed(activity: Activity) {
                }

                override fun onActivityPaused(p0activity: Activity) {
                }

                override fun onActivityStopped(activity: Activity) {
                }

                override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                }

            })
        }
    }


    interface OnSwipeBackFilterCallback{
        fun onFilter(activity: Activity):Boolean
    }
}