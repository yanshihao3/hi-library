package com.zq.hi_library.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.zq.hi_library.R
import com.zq.hilibrary.log.*
import com.zq.ui.tab.bottom.TabBottomInfo
import com.zq.ui.tab.bottom.TabBottomLayout

class HiLogDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_log_demo)


        val tabBottomLayout = findViewById<TabBottomLayout>(R.id.tabBottomLayout)
        tabBottomLayout.setTabAlpha(.8f)

        tabBottomLayout.inflateInfo(
            mutableListOf(
                TabBottomInfo(
                    "首页", "fonts/iconfont.ttf",
                    getString(R.string.if_recommend), null, "#ff656667", "#ffd44949"
                ),
                TabBottomInfo(
                    "我的", "fonts/iconfont.ttf",
                    getString(R.string.if_profile), null, "#ff656667", "#ffd44949"
                ),
                TabBottomInfo(
                    "首页", "fonts/iconfont.ttf",
                    getString(R.string.if_favorite), null, "#ff656667", "#ffd44949"
                )

            ).toList()
        )

        tabBottomLayout.addTabSelectedChangeListener { index, prevInfo, nextInfo ->
            Toast.makeText(this, "$index", Toast.LENGTH_LONG).show()
        }


        findViewById<Button>(R.id.button).setOnClickListener {
            val viewPrinter = ViewPrinter(this)
            viewPrinter.viewPrinterProvider.showFloatingView()
            YLog.log(object : LogConfig() {
                override fun includeTread(): Boolean {
                    return false
                }

                override fun stackTraceDepth(): Int {
                    return 0
                }
            }, LogType.E, "------", "5566")

            YLog.a("afda", "123", "456", 1, 2, 2, 3)
            LogManager.addPrinter(viewPrinter)
        }
    }
}