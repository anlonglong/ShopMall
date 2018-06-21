package com.aillen.shopmall.repository

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase


/**
 * Created by anlonglong on 2018/5/14.
 * Email： 940752944@qq.com
 */

@Database(entities = [(Address::class)],version = 1)
abstract class AddressDatebase:RoomDatabase() {

    abstract fun addressDao():DAO
}