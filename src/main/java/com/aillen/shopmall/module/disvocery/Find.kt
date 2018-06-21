package com.aillen.shopmall.module.main.disvocery

import cn.bmob.v3.BmobObject
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by anlonglong on 2018/4/27.
 * Email： 940752944@qq.com
 */
data class Find(val S_OID: String?,
                val user_post: String?,
                val user_name: String?,
                val user_theme: String?,
                val user_pic_url: List<String>?,
                val user_scan: Int?,
                val type: Int,
                val big_title: List<String>,
                val small_title: List<String>) : BmobObject(), MultiItemEntity {
    override fun getItemType() = type
}

enum class ItemType(val code: Int) {

    TYPE_USER(0x00) {
        override fun getTypeCode() = 0x00
    },
    TYPE_EDIT(0x01) {
        override fun getTypeCode() = 0x01
    };

    abstract fun getTypeCode(): Int
}
