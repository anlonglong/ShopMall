package com.aillen.shopmall.module.detail.head

import android.content.Intent
import android.graphics.Paint
import android.support.annotation.StringRes
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.aillen.shopmall.R
import com.aillen.shopmall.module.detail.DetailAdapter
import com.aillen.shopmall.module.store.Shop
import com.aillen.shopmall.utils.CommonUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.bottom_dialog.view.*
import kotlinx.android.synthetic.main.first_head_layout.view.*
import kotlin.properties.Delegates


/**
 * Created by anlonglong on 2018/5/4.
 * Email： 940752944@qq.com
 */
class FirstHead(contentView: View) : IHead, View.OnClickListener {

    private var view by Delegates.notNull<View>()
    private var mAdapter by Delegates.notNull<DetailAdapter>()
    private var shop by Delegates.notNull<Shop>()
    init {
        view = contentView
    }

    override fun <T> initData(data: T) {
        shop = data as Shop
        view.first_banner.setAdapter { _, view, imageUrl, _ ->
            Glide.with(view.context).load(imageUrl).into(view as ImageView)
        }
        view.first_banner.setData(R.layout.banner_item, shop.show_urls, null)
        val services = shop.service.split(',')
        view.run {
            first_name.text = shop.name
            tv_first_address.text = shop.address
            tv_first_price.text = fillData(R.string.price, shop.price)
            tv_first_price_discount.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            tv_first_price_discount.text = fillData(R.string.price, shop.price_discount)
            tv_first_postage.text = fillData(R.string.postage, shop.postage)
            tv_first_sell_num.text = fillData(R.string.sell_num, shop.sell_num)
            tv_first_service_order_risk.text = services[0]
            tv_first_service_return_goods.text = services[1]
            tv_first_service_ant.text = services[2]
        }

    }

    //@StringRes int id, Object... formatArgs
    private fun fillData(@StringRes id: Int, args: Any): String {
        return@fillData view.resources.getString(id, (args as? String)?.toString() ?: args)
    }


    override fun setAdapter(adapter: DetailAdapter) {
        mAdapter = adapter
        addHead()
    }

    override fun addHead() {
        mAdapter.addHeaderView(view)
    }



    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_first_share -> {
                toast("分享")
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share")
                intent.putExtra(Intent.EXTRA_TEXT, "这是我分享的内容")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                view.context.startActivity(Intent.createChooser(intent, "分享"))
            }
            R.id.tv_first_dialog -> {
                createDialog().show()
//                dia.takeIf {
//                    it.invoke()!=null
//                }!!.invoke().show()
            }
            R.id.tv_finish -> {
                createDialog().dismiss()
            }
        }
    }

    val dia:() ->BottomSheetDialog = {
        mDialog ?: kotlin.run {
            val dialog = BottomSheetDialog(view.context)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            val dialogView = View.inflate(view.context, R.layout.bottom_dialog, null)
            initDialogData(dialogView)
            dialogView.findViewById<TextView>(R.id.tv_finish).setOnClickListener(this)
            dialog.setContentView(dialogView)
            mDialog = dialog
            dialog   //这里我们有两种写法，当返回的是dialog时候，不用加！！
        }
    }

    override fun initListener() {
        view.tv_first_share.setOnClickListener(this)
        view.tv_first_dialog.setOnClickListener(this)
    }

    /**
     * 第一版
     */
   private  var mDialog:BottomSheetDialog?=null
   private fun createDialog_1():BottomSheetDialog{
        if (null == this.mDialog) {
            mDialog = BottomSheetDialog(view.context)
            mDialog!!.setCancelable(true)
            mDialog!!.setCanceledOnTouchOutside(true)
            val dialogView = View.inflate(view.context, R.layout.bottom_dialog, null)
            dialogView.findViewById<TextView>(R.id.tv_finish).setOnClickListener(this)
            mDialog!!.setContentView(dialogView)
            initDialogData(dialogView)
        }
        return mDialog!!
    }

    /**
     * 第二版
     */
    private fun createDialog_2():BottomSheetDialog {
        //如果mDialog==null的话，会去执行run{}代码块创建mDialog
        return mDialog ?: kotlin.run {
            val dialog = BottomSheetDialog(view.context)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            val dialogView = View.inflate(view.context, R.layout.bottom_dialog, null)
            initDialogData(dialogView)
            dialogView.findViewById<TextView>(R.id.tv_finish).setOnClickListener(this)
            dialog.setContentView(dialogView)
            mDialog = dialog
            dialog   //这里我们有两种写法，当返回的是dialog时候，不用加！！
        }
        /**
         * kotlin.run {
        val dialog = BottomSheetDialog(view.context)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        val dialogView = View.inflate(view.context, R.layout.bottom_dialog, null)
        initDialogData(dialogView)
        dialogView.findViewById<TextView>(R.id.tv_finish).setOnClickListener(this)
        dialog.setContentView(dialogView)
        mDialog = dialog
        mDialog
        }!!
         */
        // 当返回的是mDialog时候，可以确定mDialog不是null了，
        // 但是mDialog在定义的时候是可null的，而返回值不可null，
        // 所以加！！操作符把BottomSheetDialog?转换成BottomSheetDialog的

    }

    /**
     * 第三版
     */
    private fun createDialog():BottomSheetDialog{
        //如果mDialog==null的话，会去执行run{}代码块创建mDialog
        return mDialog?:kotlin.run {
              BottomSheetDialog(view.context)
             }.also {
            //also适合于对调用者进行一些属性设置的情况下使用
            it.setCancelable(true)
            it.setCanceledOnTouchOutside(true)
            val dialogView = View.inflate(view.context, R.layout.bottom_dialog, null)
            initDialogData(dialogView)
            dialogView.findViewById<TextView>(R.id.tv_finish).setOnClickListener(this)
            it.setContentView(dialogView)
            mDialog = it
            }
    }


    private fun initDialogData(dialogView: View) {
        dialogView.bottom_recycle_view.run {
            layoutManager = LinearLayoutManager(view.context)
            adapter = object :BaseQuickAdapter<String,BaseViewHolder>(android.R.layout.simple_list_item_1,shop.service.split(',')){
                override fun convert(helper: BaseViewHolder, item: String) {
                    val drawable = view.resources.getDrawable(R.drawable.detail_mid_ic_checked)
                    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                    helper.getView<TextView>(android.R.id.text1).setCompoundDrawables(drawable,null,null,null)
                    helper.getView<TextView>(android.R.id.text1).compoundDrawablePadding = CommonUtils.dp2Px(view.context,4)
                    helper.setText(android.R.id.text1,item)
                }

            }
        }

    }

    private fun toast(mag: String) {
        Toast.makeText(view.context, mag, Toast.LENGTH_SHORT).show()
    }

}
