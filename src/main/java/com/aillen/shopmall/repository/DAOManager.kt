package com.aillen.shopmall.repository

import android.arch.persistence.room.Room
import android.content.Context

/**
 * Created by anlonglong on 2018/5/14.
 * Email： 940752944@qq.com
 */
object DAOManager {
    private var instance: AddressDatebase? = null
    private const val DB_NAME = "shopmall"
    @Synchronized
    fun get(context: Context): AddressDatebase {
        if (instance == null) {
            instance = Room.databaseBuilder(context.applicationContext,
                    AddressDatebase::class.java, DB_NAME).allowMainThreadQueries().build()
        }
        return instance!!
    }
}