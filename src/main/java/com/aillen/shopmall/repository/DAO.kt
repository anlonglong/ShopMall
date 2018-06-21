package com.aillen.shopmall.repository

import android.arch.persistence.room.*


/**
 * Created by anlonglong on 2018/5/14.
 * Email： 940752944@qq.com
 */

@Dao
interface DAO {

    @Insert
    fun insert(address: Address)


    @Update
    fun update(address: Address)

    @Query("SELECT * FROM address WHERE userName= :userName")
    fun query(userName: String): List<Address>

    @Delete
    fun delete(address: Address)

}