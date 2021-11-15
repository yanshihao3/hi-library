package com.zq.hilibrary.cache

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zq.hilibrary.util.AppGlobals

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-29 16:16
 **/

@Database(entities = [Cache::class], version = 1, exportSchema = false)

abstract class CacheDatabase : RoomDatabase() {
    abstract val cacheDao: CacheDao

    companion object {
        private var database: CacheDatabase? = null

        @Synchronized
        fun getInstance(): CacheDatabase {
            if (database == null) {
                Room.databaseBuilder(AppGlobals.get()!!, CacheDatabase::class.java, "cache.db")
                    // 允许在主线程操作数据库
                    .allowMainThreadQueries()
                    //做数据库升级
                    .build()
            }
            return database!!
        }
    }

}