package com.github.yqyzxd.swipeback

import android.app.Activity
import android.os.Build
import com.github.yqyzxd.reflection.Reflection
import java.lang.reflect.Method

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: activity
 * Author: wind
 * Date: 2022/5/25 17:43
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 *  <author> <time> <version> <desc>
 *
 */

fun Activity.enableTranslucent(translucent: Boolean): Boolean {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        return setTranslucent(translucent)
    } else {
        val activityClazz = Class.forName("android.app.Activity")
        var mTranslucentConversionListenerClass: Class<*>? = null

        kotlin.run loop@{
            activityClazz.declaredClasses.forEach {
                if ("TranslucentConversionListener" == it.simpleName) {
                    mTranslucentConversionListenerClass = it
                    return@loop
                }
            }
        }

        return if (translucent) {
            var result: Boolean? = false
            mTranslucentConversionListenerClass?.let {
                val optionsClazz = Class.forName("android.app.ActivityOptions")
                result = Reflection().on(this.javaClass)
                    .method("convertToTranslucent", it, optionsClazz)
                    .catch { e -> e.printStackTrace() }
                    .invoke(this, null, null)
            }
            result ?: false
        } else {

            Reflection().on(this.javaClass)
                .method("convertFromTranslucent")
                .catch { e -> e.printStackTrace() }
                .invoke<Void>(this)
            true
        }
    }
}