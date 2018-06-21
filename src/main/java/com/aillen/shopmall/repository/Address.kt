package com.aillen.shopmall.repository

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

/**
 * Created by anlonglong on 2018/5/14.
 * Email： 940752944@qq.com
 */
@Entity(tableName = "address")
data class Address constructor(
                               @PrimaryKey(autoGenerate = true)
                               var id: Long = 0,
                               var userName: String? = null,
                               var realName: String? = null,
                               var phone: String? = null,
                               var area: String? = null,
                               var street: String? = null,
                               var address: String? = null,
                               var isDefault:Boolean? = false
):Serializable {
    constructor() : this(0)
}