package com.aillen.shopmall.module.order

import cn.bmob.v3.BmobObject

/**
 * Created by anlonglong on 2018/5/18.
 * Email： 940752944@qq.com
 */
data class Order(var U_OID: String,
                 var S_OID: List<String>,
                 var state: Int,
                 var store_name: String,
                 var order_number: String,
                 var realname: String,
                 var address: String,
                 var phone: String,
                 var express_number: String,
                 var express_type: String,
                 var express_date: String,
                 var pay_way: String,
                 var bill_type: String,
                 var bill_title: String,
                 var bill_message: String,
                 var postage: String,
                 var sum_money: String
) : BmobObject()


/**
 *  //全部
int STATE_ALL = -0x01;
//待付款
int STATE_PAY = 0x00;
//待发货
int STATE_SEND = 0x01;
//待收货
int STATE_GET = 0x02;
//待评价
int STATE_WAIT = 0x03;
//已完成
int STATE_COMPLETE = 0x04;
 */
enum class State {

    STATE_ALL {
        override fun getState() = -0x01
    },
    STATE_PAY {
        override fun getState() = 0x00
    },
    STATE_SEND {
        override fun getState() = 0x01
    },
    STATE_GET {
        override fun getState() = 0x02
    },

    STATE_WAIT {
        override fun getState() = 0X03
    },

    STATE_COMPELTE {
        override fun getState() = 0x04
    };

    abstract fun getState(): Int
}