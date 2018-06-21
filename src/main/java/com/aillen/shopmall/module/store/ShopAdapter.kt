package com.aillen.shopmall.module.store

import android.widget.ImageView
import com.aillen.shopmall.R
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.shop_item.view.*

/**
 * Created by anlonglong on 2018/5/3.
 * Email： 940752944@qq.com
 */
class ShopAdapter(list: List<Shop>?):BaseQuickAdapter<Shop,BaseViewHolder>(R.layout.shop_item,list) {

    override fun convert(helper: BaseViewHolder, item: Shop) {
        helper.itemView.tv_name.text = item.name
        helper.itemView.tv_price.text = item.price
        helper.itemView.tv_sell_num.text = item.sell_num.toString()+"人付款"
        displayImage(item.show_urls[0],helper.itemView.iv_shop)
    }
    private fun displayImage(url:String, imageView: ImageView) {
        Glide.with(mContext).load(url).into(imageView)
    }
}