package com.aillen.shopmall.module.store

import cn.bmob.v3.BmobObject

/**
 * Created by anlonglong on 2018/5/2.
 * Email： 940752944@qq.com
 */
data class Store(val love_num:Int,
                 val all_shop:Int,
                 val name:String,
                 val img_url:String,
                 val rate:Int,
                 val shop_grade:Double,
                 val store_grade:Double,
                 val delivery_grade:Double):BmobObject() {
}