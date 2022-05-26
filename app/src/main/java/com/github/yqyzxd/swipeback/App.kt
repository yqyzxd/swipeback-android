package com.github.yqyzxd.swipeback

import android.app.Application
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: App
 * Author: wind
 * Date: 2022/5/24 14:47
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 *  <author> <time> <version> <desc>
 *
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SwipeBacks.install(this)
    }
}