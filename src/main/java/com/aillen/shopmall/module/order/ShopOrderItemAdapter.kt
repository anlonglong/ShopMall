package com.aillen.shopmall.module.order

import android.widget.ImageView
import com.aillen.shopmall.R
import com.aillen.shopmall.module.store.Shop
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.order_item.view.*

/**
 * Created by anlonglong on 2018/5/22.
 * Email： 940752944@qq.com
 */
class ShopOrderItemAdapter(private  val shopList: List<Shop>): BaseQuickAdapter<Shop, BaseViewHolder>(R.layout.order_item, shopList) {

    override fun convert(helper: BaseViewHolder, item: Shop) {
        displayImage(item.show_urls[0], helper.itemView.iv_shop)
        helper.itemView.tv_name.text = item.name
        helper.itemView.tv_price.text = helper.itemView.context.resources.getString(R.string.price, item.price)
        helper.itemView.tv_postage.text = "快递：" + item.postage
        helper.itemView.tv_sell_num.text = "月售" + item.sell_num + "笔"

    }

    private fun displayImage(url: String, imageView: ImageView) {
        Glide.with(mContext).load(url).into(imageView)
    }
}