package com.aillen.shopmall.module.card

import android.text.Html
import com.aillen.shopmall.R
import com.aillen.shopmall.extensions.ctx
import com.aillen.shopmall.extensions.setHtml
import com.aillen.shopmall.utils.CommonUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.card_item.view.*

/**
 * Created by anlonglong on 2018/6/6.
 * Email： 940752944@qq.com
 */
class CardAdapter(list: MutableList<Card>?):BaseQuickAdapter<Card,BaseViewHolder>(R.layout.card_item,list) {
    override fun convert(holder: BaseViewHolder, item: Card) {
             with(holder.itemView){
                 money.setHtml(CommonUtils.fillFormatString(this.ctx,R.string.card_money,item.card_money.toString()))
                 time.text = "${item.createdAt} ~ ${item.endTime}"
                 title.text = when(item.type) {
                     Type.TYPE_COMMON.type ->{"专场券 " + item.store_name}
                     Type.TYPE_NEW.type ->{"新专场券 " + item.store_name}
                     Type.TYPE_SPECIA.type ->{"特卖专场券 " + item.store_name}
                     else ->{"unknow"}
                 }
             }
    }
}