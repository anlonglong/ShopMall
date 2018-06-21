package com.aillen.shopmall.module.disvocery

import android.view.View
import android.widget.ImageView
import com.aillen.shopmall.R
import com.aillen.shopmall.module.main.disvocery.Find
import com.aillen.shopmall.module.main.disvocery.ItemType
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.discovery_edit_item.view.*
import kotlinx.android.synthetic.main.discovery_user_item.view.*

/**
 * Created by anlonglong on 2018/4/28.
 * Email： 940752944@qq.com
 */
class DiscAdapter(list: List<Find>?) : BaseMultiItemQuickAdapter<Find, DiscAdapter.DiscHolder>(list) {


    init {
        addItemType(ItemType.TYPE_USER.getTypeCode(), R.layout.discovery_user_item)
        addItemType(ItemType.TYPE_EDIT.getTypeCode(), R.layout.discovery_edit_item)
    }

    override fun convert(helper: DiscHolder, item: Find) {
        when (item.type) {
            ItemType.TYPE_USER.getTypeCode() -> {
                helper.itemView.tv_user_name.text = item.user_name
                helper.itemView.tv_user_theme.text = item.user_theme
                helper.itemView.tv_user_post.text = item.user_post
                helper.itemView.tv_user_scan.text = item.user_scan.toString() + "人浏览"
                helper.itemView.tv_date.text = item.createdAt
                val filter = item.user_pic_url!!.filter { !it.isEmpty() }
                if (filter.size != 3) return
                displayImage(filter[0], helper.itemView.iv_find_1)
                displayImage(filter[1], helper.itemView.iv_find_2)
                displayImage(filter[2], helper.itemView.iv_find_3)
            }
            ItemType.TYPE_EDIT.getTypeCode() -> {
                val bigTitles = arrayOf(helper.itemView.tv_big_title_1, helper.itemView.tv_big_title_2, helper.itemView.tv_big_title_3)
                val smallTitles = arrayOf(helper.itemView.tv_small_title_1, helper.itemView.tv_small_title_2, helper.itemView.tv_small_title_3)
                val images = arrayOf(helper.itemView.iv_find_4, helper.itemView.iv_find_5, helper.itemView.iv_find_6)
                for (index in bigTitles.indices) {
                    bigTitles[index].text = item.big_title[index]
                    smallTitles[index].text = item.small_title[index]
                    displayImage(item.user_pic_url!![index], images[index])
                }
            }

        }
    }

    inner class DiscHolder(contentView: View) : BaseViewHolder(contentView)

    private fun displayImage(url: String, imageView: ImageView) {
        Glide.with(mContext).load(url).into(imageView)
    }

}