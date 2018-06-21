package com.aillen.shopmall.module.collection

import android.widget.ImageView
import com.aillen.shopmall.R
import com.aillen.shopmall.module.store.Shop
import com.aillen.shopmall.module.store.Store
import com.aillen.shopmall.utils.CommonUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.collection_item.view.*
import kotlinx.android.synthetic.main.shop_item.view.*

/**
 * Created by anlonglong on 2018/6/9.
 * Email： 940752944@qq.com
 */
class CollectionAdapter(list: List<Store>?): BaseQuickAdapter<Store, BaseViewHolder>(R.layout.collection_item,list) {

    override fun convert(holder: BaseViewHolder, item: Store) {
        with(holder.itemView){
            displayImage(item.img_url,iv_store_icon)
            CommonUtils.addRateImage(item.rate,ly_store_rate)
            tv_store_name.text = item.name
        }

    }
    private fun displayImage(url:String, imageView: ImageView) {
        Glide.with(mContext).load(url).into(imageView)
    }
}