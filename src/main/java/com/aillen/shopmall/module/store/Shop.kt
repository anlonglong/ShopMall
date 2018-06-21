package com.aillen.shopmall.module.store

import cn.bmob.v3.BmobObject
import java.io.Serializable

/**
 * Created by anlonglong on 2018/5/2.
 * Email： 940752944@qq.com
 */
class Shop(val name:String,
           val price:String,
           val price_discount: String,
           val postage:String,
           val sell_num:Int,
           val address:String,
           val show_urls:List<String>,
           val service:String,
           val S_OID:String,
           val detail_urls:List<String>):BmobObject(),Serializable
/**
 * //商品名称
public String name;
//打折价
public String price;
//打折价+删除线
public String price_discount;
//邮费
public String postage;
//月销
public int sell_num;
//商品地址
public String address;
//商品详细图
public List<String> show_urls;
//商品服务保障
public String service;
//店铺id
public String S_OID;
//详细信息图片
public List<String> detail_urls;
 **/