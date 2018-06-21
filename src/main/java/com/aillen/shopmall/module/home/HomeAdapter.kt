package com.aillen.shopmall.module.home

import com.aillen.shopmall.R
import com.aillen.shopmall.module.store.Shop
import com.aillen.shopmall.utils.CommonUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.shop_item.view.*

/**
 * Created by anlonglong on 2018/6/14.
 * Email： 940752944@qq.com
 */
class HomeAdapter(private val shopList:List<Shop>?):BaseQuickAdapter<Shop,BaseViewHolder>(R.layout.shop_item,shopList) {

    override fun convert(holder: BaseViewHolder, item: Shop) {
        with(holder.itemView){
            tv_name.text = item.name
           tv_price.text = item.price
            tv_sell_num.text = item.sell_num.toString()+"人付款"
            CommonUtils.disPlayImageWithGlide(context,item.show_urls[0],iv_shop)
        }
    }
}