package com.github.yqyzxd.swipeback

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: SwpieBackActivity
 * Author: wind
 * Date: 2022/5/25 14:58
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 *  <author> <time> <version> <desc>
 *
 */

class SwipeBackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_back)
        val tool_bar=findViewById<Toolbar>(R.id.tool_bar)
        tool_bar.title="Swipe"
        setSupportActionBar(tool_bar)
        tool_bar
        findViewById<View>(R.id.tv).setOnClickListener {

           //Toast.makeText(MainActivity@this,"click",Toast.LENGTH_SHORT).show()
            MainActivity@this.finish()
        }
    }



}