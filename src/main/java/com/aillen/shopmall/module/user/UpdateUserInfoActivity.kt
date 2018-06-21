package com.aillen.shopmall.module.user

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.view.View
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import kotlinx.android.synthetic.main.activity_update_user_info.*
import org.greenrobot.eventbus.EventBus

class UpdateUserInfoActivity : BaseActivity(), View.OnClickListener {

    companion object {
        fun start(ctx:Context) = ctx.startActivity(Intent(ctx,UpdateUserInfoActivity::class.java))
    }

    override fun initView() {
         if (UserManager.isLogin) {
             tv_select_gender.text = if(UserManager.getCurrentUser()!!.sex) "男" else "女"
             tv_select_age.text = UserManager.getCurrentUser()!!.age.toString()
         }
    }

    override fun initListener() {
        tv_select_gender.setOnClickListener(this)
        tv_select_age.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        accomplish.setOnClickListener(this)
    }



    override fun onClick(v: View) {
        var dialog:AlertDialog?=null
         when(v.id){
             R.id.tv_select_gender ->{

                 val dia = AlertDialog.Builder(this)
                 val arrayOf = arrayOf("男", "女")
                 with(dia) {
                     setTitle("请选择")
                     setCancelable(true)
                     setSingleChoiceItems(arrayOf, -1) { _, which ->
                         tv_select_gender.text = arrayOf[which]
                         dialog?.dismiss()
                     }
                     dialog = create()
                     dialog?.show()
                 }

             }
             R.id.tv_select_age ->{
                 val dia = AlertDialog.Builder(this)
                 val arrayOf = arrayOfNulls<String>(37)
                 for(i in 0..36) {
                     arrayOf[i]  =(i+14).toString()
                 }
                 with(dia) {
                     setTitle("请选择")
                     setCancelable(true)
                     setSingleChoiceItems(arrayOf,-1) { _, which ->
                         tv_select_age.text = arrayOf[which]
                         dialog?.dismiss()
                     }
                     dialog = create()
                     dialog?.show()
                 }
             }
             R.id.accomplish -> {updateUserInfo()}

             R.id.iv_back ->{finish()}
         }
    }

    private fun updateUserInfo() {
        val gender = tv_select_gender.text == "男"
        val user = User(gender, tv_select_age.text.toString().toInt(), 1)
        user.update(UserManager.objId,object:UpdateListener(){
            override fun done(e: BmobException?) {
                toast(if (e == null) "更新成功" else "更新失败：{${e.message}}")
                if (e == null){
                    EventBus.getDefault().post(UpdateUserInfo())
                    finish()
                }
            }
        })
    }

    override fun getLayoutId() = R.layout.activity_update_user_info


}
