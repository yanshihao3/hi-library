package com.zq.hilibrary.cache

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object Storage {

    fun <T> saveCache(key: String, body: T) {
        val cache = Cache()
        cache.key = key
        cache.data = toByteArray(body)
        CacheDatabase.getInstance().cacheDao.saveCache(cache)
    }


    fun <T> getCache(key: String): T? {
        val cache = CacheDatabase.getInstance().cacheDao.getCache(key)
        return (if (cache?.data != null) {
            toObject(cache.data)
        } else null) as? T
    }

    fun deleteCache(key: String) {
        val cache = Cache()
        cache.key = key
        CacheDatabase.getInstance().cacheDao.deleteCache(cache)
    }


    private fun <T> toByteArray(body: T): ByteArray {
        val baos: ByteArrayOutputStream? = ByteArrayOutputStream()
        val oos: ObjectOutputStream? = ObjectOutputStream(baos)
        try {
            oos!!.writeObject(body)
            oos!!.flush()
            oos.flush()
            return baos!!.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            baos?.close()
            oos?.close()
        }
        return ByteArray(0)
    }

    private fun toObject(data: ByteArray?): Any? {
        val bais: ByteArrayInputStream? = ByteArrayInputStream(data)
        val ois: ObjectInputStream? = ObjectInputStream(bais)
        try {
            return ois!!.readObject()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            ois?.close()
            bais?.close()
        }
        return null
    }
}