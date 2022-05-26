package com.github.yqyzxd.swipeback

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: ISwipeBackCallback
 * Author: wind
 * Date: 2022/5/25 16:25
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 *  <author> <time> <version> <desc>
 *
 */
interface ISwipeListener {

    fun onSwiping(left:Int,state:SwipeState)

}

enum class SwipeState{
    START,SWIPING,FINISH,RESET
}