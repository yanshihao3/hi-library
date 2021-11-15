package com.zq.hilibrary.log

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zq.hilibrary.R

/**
 * @program: hi-library
 *
 * @description:将log显示在界面上
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-15 18:45
 **/
class ViewPrinter(private val activity: Activity) : LogPrinter {

    private val recyclerView: RecyclerView = RecyclerView(activity)
    private val decorView = activity.window.decorView
    private val adapter = LogAdapter(activity)
    val viewPrinterProvider by lazy {
        ViewPrinterProvider(decorView as FrameLayout, recyclerView)
    }

    init {

        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    override fun print(config: LogConfig, level: Int, tag: String, printString: String) {
        adapter.addItem(LogMo(System.currentTimeMillis(), level, tag, printString))
        recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
    }

}

class LogAdapter(context: Context) : RecyclerView.Adapter<LogViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private val logs = mutableListOf<LogMo>()

    fun addItem(logMo: LogMo) {
        logs.add(logMo)
        notifyItemInserted(logs.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val itemView = inflater.inflate(R.layout.hilog_item, parent, false)
        return LogViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val logMo = logs[position]
        val color = getHighlightColor(logMo.level)
        holder.tagView.setTextColor(color)
        holder.messageView.setTextColor(color)

        holder.tagView.text = logMo.flattened
        holder.messageView.text = logMo.log
    }

    /**
     * 跟进log级别获取不同的高了颜色
     *
     * @param logLevel log 级别
     * @return 高亮的颜色
     */
    private fun getHighlightColor(logLevel: Int) = when (logLevel) {
        LogType.V -> Color.parseColor("#ffbbbbbb")
        LogType.D -> Color.parseColor("#ffffffff")
        LogType.I -> Color.parseColor("#ff6a8759")
        LogType.W -> Color.parseColor("#ffbbb529")
        LogType.E -> Color.parseColor("#ffff6b68")
        else -> Color.parseColor("#ffffff00")
    }

    override fun getItemCount() = logs.size


}

class LogViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tagView: TextView = itemView.findViewById(R.id.tag)
    val messageView: TextView = itemView.findViewById(R.id.message)

}