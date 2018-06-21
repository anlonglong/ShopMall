package com.aillen.shopmall.module.detail.bottomdialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.view.View
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.aillen.shopmall.R
import com.aillen.shopmall.extensions.ctx
import com.aillen.shopmall.module.detail.DetailActivity
import com.aillen.shopmall.module.user.UserManager
import com.aillen.shopmall.module.store.Shop
import com.aillen.shopmall.utils.CommonUtils
import kotlinx.android.synthetic.main.cart.view.*
import org.jetbrains.anko.toast
import kotlin.properties.Delegates


/**
 * Created by anlonglong on 2018/5/25.
 * Email： 940752944@qq.com
 */
@SuppressLint("ValidFragment")
/**
 * 这个BottomDialog有个bug，耗了一个星期了，还没解决，先放一放。
 */
class BottomDialog() : BottomSheetDialogFragment() {

    companion object {
        private var dialog:BottomDialog? = null
        fun show( activity: FragmentActivity){
           createDialog(activity).show()
        }

       private fun createDialog(activity: FragmentActivity):BottomDialog = dialog?:kotlin.run {
           val dia = BottomDialog(activity)
           dialog =  dia
           println("createDialog ")
           dia
      }

        fun dimissDialog(){
            dialog?.dismiss()
        }
    }

    private var mLayoutId:Int = 0
    private var mAty: FragmentActivity by Delegates.notNull()

    constructor(activity: FragmentActivity, contentId:Int = R.layout.cart):this() {
        this.mAty = activity
        this.mLayoutId = contentId
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(mAty)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        val contentView = View.inflate(context,mLayoutId,null)
        dialog.setContentView(contentView)
        initData(contentView)
        initListener(contentView)
        return dialog
    }

    private fun initListener(contentView: View) {
        contentView.complish.setOnClickListener { dimissDialog()}
    }

   @Synchronized fun  show(){
       val manager = mAty.supportFragmentManager
       if (!mAty.isFinishing){
           val ft = manager.beginTransaction()
           if (this.isAdded) {
               ft.remove(this).commitAllowingStateLoss()
           }
           ft.add(this,"tag")
           ft.commitAllowingStateLoss()
           ft.show(this)
       }

    }

    fun dimissDialog(){
        this.dismissAllowingStateLoss()
    }

    private fun initData(contentView: View) {
        val cartOids = UserManager.cartOid
        if (cartOids.isEmpty()){
            CommonUtils.showProgress(contentView.ctx,false,contentView.bottom_dialog_progress,contentView.cart_list)
            return
        }
        CommonUtils.showProgress(contentView.ctx,true,contentView.bottom_dialog_progress,contentView.cart_list)
         val query = BmobQuery<Shop>()
        query.cachePolicy = BmobQuery.CachePolicy.NETWORK_ONLY
        query.setLimit(80)
        query.order("S_OID")
        query.addWhereContainedIn("objectId",cartOids)
        query.findObjects(object : FindListener<Shop>(){
            override fun done(list: MutableList<Shop>?, e: BmobException?) {
                CommonUtils.showProgress(contentView.ctx,false,contentView.bottom_dialog_progress,contentView.cart_list)
                when(e) {
                       null ->{
                           if (list!!.isEmpty()){
                              contentView.complish.visibility = View.GONE
                              contentView.tv_dialog_empty_hint.visibility = View.VISIBLE
                               //mAty.toast("购物车空空如也")
                               return
                           }
                           contentView.complish.visibility = View.VISIBLE
                           val bottomDialogAdapter = BottomDialogAdapter(list)
                           contentView.cart_list.layoutManager = LinearLayoutManager(mAty,VERTICAL,false)
                           contentView.cart_list.adapter = bottomDialogAdapter
                           bottomDialogAdapter.setOnItemClickListener{adapter, view, position ->
                               BottomDialog.dimissDialog()
                               DetailActivity.start(this@BottomDialog.activity,adapter.getItem(position) as Shop)
                           }
                       }
                       else ->{mAty.toast(e.message!!)}
                   }

            }

        })
    }



    private fun getLayoutId() = R.layout.cart

}