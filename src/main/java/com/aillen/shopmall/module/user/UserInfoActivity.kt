package com.aillen.shopmall.module.user

import android.content.Context
import android.content.Intent
import android.view.View
import cn.bmob.v3.BmobUser
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.login.LoginEvent
import kotlinx.android.synthetic.main.activity_logout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class UserInfoActivity : BaseActivity(),View.OnClickListener {



    companion object {
        fun start(ctx:Context){
            ctx.startActivity(Intent(ctx,UserInfoActivity::class.java))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateUserInfo(up:UpdateUserInfo){
           initView()
    }

    override fun initView() {
        val currentUser = UserManager.getCurrentUser()
        stv_account.setRightString(currentUser?.username)
        iv_back.setOnClickListener(this)
        stv_gender.setRightString(if(currentUser!!.sex) "男" else "女")
        stv_age.setRightString(currentUser?.age.toString())
    }

    override fun initListener() {
        stv_user_info.setOnClickListener(this)
        stv_location_manager.setOnClickListener(this)
        logout.setOnClickListener(this)
    }

    override fun onClick(v: View) {

        when(v.id) {
            R.id.logout ->{
                BmobUser.logOut()
                toast("退出登录成功")
                UserManager.isLogin = false
                EventBus.getDefault().post(User(true, -1, 0))
                EventBus.getDefault().post(LoginEvent(false))
                finish()
            }

            R.id.stv_user_info -> {UpdateUserInfoActivity.start(this)}

            R.id.stv_location_manager -> {AddressListActivity.start(this)}

            R.id.iv_back -> {finish()}
        }
    }


    override fun getLayoutId() = R.layout.activity_logout

    override fun enableEventBus() = true


}

class UpdateUserInfo
