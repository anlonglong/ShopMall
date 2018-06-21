package com.aillen.shopmall.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import kotlin.reflect.KProperty

/**
 * Created by anlonglong on 2018/5/9.
 * Email： 940752944@qq.com
 */

object DelegateExt {

    fun <T> preference(ctx: Context, key: String, default: T) = Preference(ctx, key, default)
}




class Preference<T>(private val ctx: Context, private val key: String, private val default: T) {

    companion object {
        private const val NAME = "SHOP_MALL"
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = get(key, default)

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = put(key, value)

    private val sp by lazy { ctx.getSharedPreferences(NAME, MODE_PRIVATE) }
    private fun <T> put(key: String, value: T) = with(sp.edit()) {
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Float -> putFloat(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            else -> throw IllegalArgumentException("不支持的数据类型=> $value")
        }.apply()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> get(key: String, default: T): T = sp.run {
        val value: Any = when (default) {
            is String -> getString(key, default)
            is Int -> getInt(key, default)
            is Float -> getFloat(key, default)
            is Boolean -> getBoolean(key, default)
            is Long -> getLong(key, default)
            else -> throw IllegalArgumentException("不支持的数据类型=> $default")
        }
        return value as T
    }
}