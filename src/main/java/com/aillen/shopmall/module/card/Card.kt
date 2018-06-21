package com.aillen.shopmall.module.card

import cn.bmob.v3.BmobObject

/**
 * Created by anlonglong on 2018/6/6.
 * Email： 940752944@qq.com
 */
data class Card(var S_OID:String?,
                var card_money:String?,
                var store_name:String,
                var img_url:String?,
                var endTime:String?,
                var type:Int?):BmobObject()
enum class Type(val type:Int){
    TYPE_COMMON(0x00),
    TYPE_SPECIA(0X01),
    TYPE_NEW(0X02);
}