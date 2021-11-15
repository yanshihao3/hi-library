package com.zq.hilibrary.executor

import android.os.Handler
import android.os.Looper
import androidx.annotation.IntRange
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantLock

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-24 15:24
 **/

object YExecutor {

    var isPause: Boolean = false
    private lateinit var executor: ThreadPoolExecutor
    private val lock = ReentrantLock()
    private val pauseCondition = lock.newCondition()
    private val mHandler = Handler(Looper.getMainLooper())

    init {
        val cpuCount = Runtime.getRuntime().availableProcessors()
        val corePoolSize = cpuCount + 1
        val maxPoolSize = cpuCount * 2 + 1
        val blockingQueue = PriorityBlockingQueue<Runnable>()
        val keepAlive = 30L
        val unit = TimeUnit.SECONDS
        val seq = AtomicLong()
        val threadFactory = ThreadFactory {
            val thread = Thread()
            thread.name = "executor-${seq.getAndIncrement()}"
            return@ThreadFactory thread
        }


        executor = object : ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            keepAlive,
            unit,
            blockingQueue,
            threadFactory
        ) {
            override fun beforeExecute(t: Thread?, r: Runnable?) {
                super.beforeExecute(t, r)
                if (isPause) {
                    try {
                        lock.lock()
                        pauseCondition.await()
                    } finally {
                        lock.unlock()
                    }
                }
            }

            override fun afterExecute(r: Runnable?, t: Throwable?) {
                super.afterExecute(r, t)
                //监控线程池耗时任务，线程创建数量，正在运行的数量
            }
        }
    }


    fun execute(@IntRange(from = 0, to = 10) priority: Int = 0, runnable: Runnable) {
        executor.execute(PriorityRunnable(priority, runnable))
    }

    @Synchronized
    fun pause() {
        isPause = true
    }

    @Synchronized
    fun resume() {
        isPause = false
        try {
            lock.lock()
            pauseCondition.signalAll()
        } finally {
            lock.unlock()
        }
    }

    abstract class Callable<T> : Runnable {
        override fun run() {
            mHandler.post { onPrepare() }
            val t = onBackground()
            mHandler.post { onCompleted(t) }
        }

        /**
         * 主线程中执行
         */
        abstract fun onPrepare()
        abstract fun onBackground(): T

        /**
         * 主线程中执行
         */
        abstract fun onCompleted(t: T)

    }


    class PriorityRunnable(private val priority: Int, private val runnable: Runnable) :
        Runnable,
        Comparable<PriorityRunnable> {
        override fun run() {
            runnable.run()
        }

        override fun compareTo(other: PriorityRunnable): Int {
            return this.priority - other.priority
        }

    }
}

