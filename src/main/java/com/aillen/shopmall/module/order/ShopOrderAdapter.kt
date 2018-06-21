package com.aillen.shopmall.module.order

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationCompat.BADGE_ICON_LARGE
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aillen.shopmall.R
import com.aillen.shopmall.extensions.ctx
import com.aillen.shopmall.module.pay.PayActivity
import com.aillen.shopmall.module.store.StoreActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.order_all_item.view.*

/**
 * Created by anlonglong on 2018/5/22.
 * Email： 940752944@qq.com
 */
class ShopOrderAdapter(private val shopOrder: List<OrderModel.ShopOrder>) : BaseQuickAdapter<OrderModel.ShopOrder, BaseViewHolder>(R.layout.order_all_item, shopOrder), View.OnClickListener {

    override fun convert(holder: BaseViewHolder, item: OrderModel.ShopOrder) {
        val order = item.order!!
        holder.itemView.tv_store_name.text = order.store_name
        holder.itemView.tv_sum_money.text = holder.itemView.context.resources.getString(R.string.price, order.sum_money)
        holder.itemView.lv_order_item.layoutManager = LinearLayoutManager(holder.itemView.context)
        val shopOrderItemAdapter = ShopOrderItemAdapter(item.shopList)
        holder.itemView.lv_order_item.adapter = shopOrderItemAdapter
        shopOrderItemAdapter.setOnItemClickListener { _, view, _ ->
            OrderDetailActivity.start(view.ctx, item)
        }
        holder.itemView.ly_store.setOnClickListener(this)
        holder.itemView.ly_store.tag = item
        holder.itemView.tv_order.setOnClickListener(this)
        holder.itemView.tv_order.tag = item
        when (order.state) {
            State.STATE_GET.getState() -> {
                holder.itemView.tv_state.text = "卖家已发货"
                holder.itemView.tv_order.text = "确认收货"
                holder.itemView.tv_state.setTextColor(Color.parseColor("#333333"))

            }
            State.STATE_PAY.getState() -> {

                holder.itemView.tv_state.text = "您还未支付"
                holder.itemView.tv_order.text = "马上付款"
                holder.itemView.tv_state.setTextColor(Color.parseColor("#333333"))

            }
            State.STATE_SEND.getState() -> {

                holder.itemView.tv_state.text = "卖家未发货"
                holder.itemView.tv_order.text = "提醒发货"
                holder.itemView.tv_state.setTextColor(Color.parseColor("#333333"))

            }
            State.STATE_WAIT.getState() -> {

                holder.itemView.tv_state.text = "买家已收货"
                holder.itemView.tv_order.text = "马上评价"
                holder.itemView.tv_state.setTextColor(Color.parseColor("#333333"))

            }
            State.STATE_COMPELTE.getState() -> {
                holder.itemView.tv_state.text = "交易已完成"
                holder.itemView.tv_order.text = "删除订单"
                holder.itemView.tv_state.setTextColor(Color.RED)

            }
        }
    }

    override fun onClick(v: View) {
        val so = v.tag as OrderModel.ShopOrder
        when (v.id) {
            R.id.ly_store -> {
                StoreActivity.start(v.ctx, so.shopList[0].S_OID)
            }
            R.id.tv_order -> {
                when (so.order!!.state) {
                    State.STATE_PAY.getState() -> {
                        PayActivity.start(v.ctx, so)
                    }
                    State.STATE_SEND.getState() -> {
                        sendNotification()
                    }
                    State.STATE_WAIT.getState() -> {
                        EvaluateActivity.start(v.ctx,so.shopList[0])
                    }
                    State.STATE_COMPELTE.getState() -> {
                        println("STATE_COMPELTE")
                    }
                }
            }

        }
    }


    private fun sendNotification() {
        //通知开发文档https://developer.android.google.cn/guide/topics/ui/notifiers/notifications#ManageChannels
        val NOTIFICATION_CHANNEL_ID = "0x01"
        val NOTIFICATION_CHANNEL_NAME = "ShopMall"
        val nfm = mContext.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            with(notificationChannel) {
                setShowBadge(true)
                enableVibration(true)
                vibrationPattern = LongArray(4) { i -> (i * 100).toLong() }
                enableLights(true)
                description = "desc"
                lightColor = Color.RED
            }
            nfm.createNotificationChannel(notificationChannel)
        }
        val build = with(NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)) {
            setAutoCancel(true)
            color = mContext.resources.getColor(R.color.orange)
            setSmallIcon(R.drawable.ic_msg_o)
            setContentTitle("来自ShopMall的通知")
            setContentText("消息已发送")
            setTicker("通知来了")
            setBadgeIconType(BADGE_ICON_LARGE)
            setDefaults(Notification.DEFAULT_ALL)
            setLargeIcon(BitmapFactory.decodeResource(mContext.resources, R.drawable.splash))
            setContentIntent(PendingIntent.getActivity(mContext, 0, Intent(), FLAG_ONE_SHOT))
            setVibrate(LongArray(4) { i -> (i * 100).toLong() })
            setLights(0xff0000, 300, 0)
            setSound(Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "5"))
        }.build()
        build.flags = Notification.FLAG_AUTO_CANCEL
        nfm.notify(1, build)

    }

}