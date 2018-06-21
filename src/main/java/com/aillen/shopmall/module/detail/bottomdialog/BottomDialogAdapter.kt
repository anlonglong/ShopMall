package com.aillen.shopmall.module.detail.bottomdialog

import android.content.Context
import android.graphics.Paint
import android.support.annotation.StringRes
import com.aillen.shopmall.R
import com.aillen.shopmall.extensions.ctx
import com.aillen.shopmall.module.store.Shop
import com.aillen.shopmall.utils.CommonUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.bottom_dialog_item.view.*

/**
 * Created by anlonglong on 2018/5/28.
 * Email： 940752944@qq.com
 */
class BottomDialogAdapter(list: MutableList<Shop>):BaseQuickAdapter<Shop,BaseViewHolder>(R.layout.bottom_dialog_item,list) {
    override fun convert(holder: BaseViewHolder, item: Shop) {
        with(holder.itemView) {
                CommonUtils.disPlayImageWithGlide(ctx,item.show_urls[0],iv_shop)
                tv_name.text = item.name
                tv_store_name.text
                tv_price.text = CommonUtils.fillFormatString(mContext,R.string.price,item.price)
                tv_postage.text = CommonUtils.fillFormatString(mContext,R.string.postage,item.postage)
                tv_price_discount.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                tv_price_discount.text = CommonUtils.fillFormatString(mContext,R.string.price,item.price_discount)
                tv_sell_num.text = CommonUtils.fillFormatString(mContext,R.string.sell_num,item.sell_num)
        }
    }


}