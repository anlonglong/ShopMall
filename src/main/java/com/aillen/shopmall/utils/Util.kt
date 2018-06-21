@file:JvmName("Utils")

package com.aillen.shopmall.utils

import android.content.Context
import android.support.annotation.RawRes
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.StringWriter
import java.util.*

/**
 * Created by anlonglong on 2018/5/30.
 * Email： 940752944@qq.com
 */

/**
 * 读取asset中后缀名为.properties文件中的值
 * eg:name=allon
 * key=name
 * 输出value=allon
 */
fun getPropertyValue(ctx: Context, fileName: String, key: String): String {
    val properties = Properties()
    return try {
        properties.load(ctx.resources.assets.open(fileName))
        properties.getProperty(key)
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}

/**
 * 读取asset中后缀名为.properties文件中的键的集合
 */
fun getPropertyKeySet(ctx: Context, fileName: String): Set<String> {
    val properties = Properties()
    return try {
        properties.load(ctx.resources.assets.open(fileName))
        properties.stringPropertyNames()
    } catch (e: IOException) {
        e.printStackTrace()
        setOf()
    }
}


/**
 *读取res/raw文件夹中的某个文本文件，并以String的形式输出。
 */

fun getStringFromRaw(ctx: Context, @RawRes rawResInt: Int, chartSet: String = "UTF-8"): String {
    val inputStream = ctx.resources.openRawResource(rawResInt)
    val writer = StringWriter()
    val buffer = CharArray(size = 1024)
    var pointer = 0
    return try {
        val reader = BufferedReader(InputStreamReader(inputStream, chartSet))
        while (pointer != -1) {
            writer.write(buffer,0,pointer)
            pointer = reader.read(buffer)
        }
        writer.toString()
    } catch (e: IOException) {
        e.printStackTrace()
        e.message!!
    } finally {
        try {
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

