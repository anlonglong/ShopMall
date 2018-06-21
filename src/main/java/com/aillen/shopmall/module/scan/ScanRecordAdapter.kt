package com.aillen.shopmall.module.scan

import android.widget.ImageView
import com.aillen.shopmall.R
import com.aillen.shopmall.module.store.Shop
import com.aillen.shopmall.module.store.Store
import com.aillen.shopmall.utils.CommonUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.collection_item.view.*
import kotlinx.android.synthetic.main.order_item.view.*

/**
 * Created by anlonglong on 2018/6/9.
 * Email： 940752944@qq.com
 */
class ScanRecordAdapter(list: MutableList<Shop>): BaseQuickAdapter<Shop, BaseViewHolder>(R.layout.order_item,list) {

    override fun convert(holder: BaseViewHolder, item: Shop) {
        with(holder.itemView){
             displayImage(item.show_urls[0],iv_shop)
             tv_name.text = item.name
            tv_price.text = CommonUtils.fillFormatString(this.context,R.string.price,item.price)
            tv_postage.text = CommonUtils.fillFormatString(this.context,R.string.postage,item.postage)
            tv_sell_num.text = CommonUtils.fillFormatString(this.context,R.string.pay_count,item.sell_num)
        }

    }
    private fun displayImage(url:String, imageView: ImageView) {
        Glide.with(mContext).load(url).into(imageView)
    }
}