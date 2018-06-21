package com.aillen.shopmall.module.user

import cn.bmob.v3.BmobUser

/**
 * Created by anlonglong on 2018/5/10.
 * Email： 940752944@qq.com
 */
data class User(val sex: Boolean,
                val age: Int,
                val rate: Int,
                val love_oid: List<String>,
                var cart_oid: List<String>,
                val collection_oid: List<String>,
                val card_oid: List<String>,
                val scan_record_oid: List<String>
) : BmobUser() {
    constructor(sex: Boolean, age: Int, rate: Int) : this(
            sex,
            age,
            rate,
            arrayListOf<String>(),
            arrayListOf<String>(),
            arrayListOf<String>(),
            arrayListOf<String>(),
            arrayListOf<String>()
    )

}