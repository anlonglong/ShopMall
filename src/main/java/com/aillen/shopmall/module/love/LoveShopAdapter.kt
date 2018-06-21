package com.aillen.shopmall.module.love

import android.graphics.Paint
import com.aillen.shopmall.R
import com.aillen.shopmall.module.store.Shop
import com.aillen.shopmall.utils.CommonUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.love_shop_item.view.*

/**
 * Created by anlonglong on 2018/5/25.
 * Email： 940752944@qq.com
 */
class LoveShopAdapter(loveShopList: List<Shop>):BaseQuickAdapter<Shop,BaseViewHolder>(R.layout.love_shop_item,loveShopList) {

    override fun convert(holder: BaseViewHolder, item: Shop) {
        with(holder.itemView) {
            CommonUtils.disPlayImageWithGlide(mContext,item.show_urls[0],iv_shop)
            tv_name.text = item.name
            tv_price.text = CommonUtils.fillFormatString(mContext,R.string.price,item.price)
            tv_postage.text = CommonUtils.fillFormatString(mContext,R.string.postage,item.postage)
            tv_price_discount.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            tv_price_discount.text = CommonUtils.fillFormatString(mContext,R.string.price,item.price_discount)
            tv_sell_num.text = CommonUtils.fillFormatString(mContext,R.string.sell_num,item.sell_num)

        }
    }
}