package com.zq.hilibrary.cache

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-29 16:09
 **/

@Dao
interface CacheDao {

    @Query("select * from table_cache where 'key'=:key")
    fun getCache(key: String): Cache

    @Delete(entity = Cache::class)
    fun deleteCache(cache: Cache)

    @Insert(entity = Cache::class, onConflict = OnConflictStrategy.REPLACE)
    fun saveCache(cache: Cache)

}