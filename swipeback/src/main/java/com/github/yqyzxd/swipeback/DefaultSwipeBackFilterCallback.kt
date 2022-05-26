package com.github.yqyzxd.swipeback

import android.app.Activity

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: DefaultSwipeBackFilterCallback
 * Author: wind
 * Date: 2022/5/26 10:59
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 *  <author> <time> <version> <desc>
 *
 */
class DefaultSwipeBackFilterCallback :SwipeBacks.OnSwipeBackFilterCallback{
    private var mLauncherClassName:String?=null

    override fun onFilter(activity: Activity): Boolean {


        if (mLauncherClassName == null){
            activity.packageManager.getLaunchIntentForPackage(activity.packageName)?.component?.apply {
                mLauncherClassName=className
            }
        }

        return activity.javaClass.canonicalName == mLauncherClassName

    }
}