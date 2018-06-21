package com.aillen.shopmall.module.main.mine

import android.view.View
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseFragment
import com.aillen.shopmall.extensions.ctx
import com.aillen.shopmall.module.card.CardActivity
import com.aillen.shopmall.module.collection.CollectionActivity
import com.aillen.shopmall.module.feedback.FeedbackActivity
import com.aillen.shopmall.module.love.LoveActivity
import com.aillen.shopmall.module.user.User
import com.aillen.shopmall.module.user.UserManager
import com.aillen.shopmall.module.order.OrderActivity
import com.aillen.shopmall.module.scan.ScanRecordActivity
import com.aillen.shopmall.module.service.ServiceActivity
import com.aillen.shopmall.module.login.LoginActivity
import com.aillen.shopmall.module.login.LoginObervable
import com.aillen.shopmall.module.login.LoginObserver
import com.aillen.shopmall.module.user.UserInfoActivity
import com.aillen.shopmall.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_mine.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * Created by anlonglong on 2018/4/26.
 * Email： 940752944@qq.com
 */
class MineFragment : BaseFragment(), View.OnClickListener{

    override fun enableEventBus() = true

    companion object {
        fun getInstance() = MineFragment()
    }

    override fun initView() {
        login_state.text = if(UserManager.isLogin) UserManager.getCurrentUser()?.username else "您还没有登录"
        if (UserManager.isLogin) {
            CommonUtils.addRateImage(UserManager.getCurrentUser()!!.rate,mine_rate_container)
        }else{
            CommonUtils.addRateImage(-1,mine_rate_container)
        }
    }

    override fun intListener() {
        mine_login.setOnClickListener(this)
        order.setOnClickListener(this)
        focus.setOnClickListener(this)
        mine_card.setOnClickListener(this)
        mine_collection.setOnClickListener(this)
        mine_history.setOnClickListener(this)
        online_service.setOnClickListener(this)
        feedback.setOnClickListener(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
     fun onRegisterEvent(user: User){
        initView()
    }

    override fun loadData(mRootView: View?) {
        println("result = "+ UserManager.isLogin)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.mine_login ->{
                if(UserManager.isLogin) UserInfoActivity.start(v.context) else LoginActivity.start(v.context)
                }
            R.id.order ->{OrderActivity.start(v.context,true)}
            R.id.focus ->{ LoveActivity.start(v.ctx)}
            R.id.mine_card ->{CardActivity.start(v.ctx,true)}
            R.id.mine_collection ->{ CollectionActivity.start(v.ctx)}
            R.id.mine_history ->{ ScanRecordActivity.start(v.ctx)}
            R.id.online_service ->{ ServiceActivity.strat(v.ctx)}
            R.id.feedback ->{ FeedbackActivity.start(v.ctx)}
        }
    }

    override fun getLayoutId() = R.layout.fragment_mine
}