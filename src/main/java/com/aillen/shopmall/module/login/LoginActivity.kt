package com.aillen.shopmall.module.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.main.shoppingcart.ShoppingCartFragment
import com.aillen.shopmall.module.shoppingcart.CartViewModel
import com.aillen.shopmall.module.user.User
import com.aillen.shopmall.module.user.UserManager
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus

class LoginActivity : BaseActivity(), UserContract.UserView {

    private val observable: LoginObervable by lazy { LoginObervable() }
    companion object {
        fun start(ctx:Context){
            ctx.startActivity(Intent(ctx, LoginActivity::class.java))
        }
    }

    override fun initListener() {
        email_sign_in_button.setOnClickListener {
            login(user_name.text.toString(),password.text.toString())
        }
        email_sign_in_register.setOnClickListener { RegisterActivity.start(this) }
    }


    //第二版 kotlin版
    private fun login(userName: String, pwd: String) {
        user_name.error = null
        password.error = null

        if (!isUserNameValid(userName)){
            user_name.error = "用户名不合法"
            user_name.requestFocus()
            return@login
        }

         if (!isPasswordValid(pwd)){
            password.error = "密码太短"
             password.requestFocus()
             return@login
        }

        UserPresenter(this).login(userName,pwd)

    }

    //第一版，java版本 哪个不满足就让光标聚焦在哪一个view上面
    private fun login_1(userName: String, pwd: String) {
        user_name.error = null
        password.error = null

        var focuse:View? = null
        var cancel = false
        if (!isUserNameValid(userName)) {
            focuse = user_name
            user_name.error = "用户名不合法"
            cancel = true
        }

        if (cancel) {
            focuse?.requestFocus()
            return
        }

        if (!isPasswordValid(pwd)) {
            focuse = password
            password.error = "密码太短"
            cancel = true
        }

        if (cancel) {
            focuse?.requestFocus()
            return
        }
        showProgress(true)
        UserPresenter(this).login(userName,pwd)
    }


    override fun initView() {
        observable.addObserver(ShoppingCartFragment.getInstance())
    }
    override fun onLogin(user: User) {
        //登录成功通知所有观察者
        showProgress(false)
        UserManager.isLogin = true
        EventBus.getDefault().post(user)
        EventBus.getDefault().post(LoginEvent(true))
        finish()

        /**
         * 登录成功以后通知[ShoppingCartFragment]中的update方法更新数据
         * **/
        observable.changed = true
        observable.notifyObservers(user,true)

        //getHandler().postDelayed({ShoppingCartFragment.getInstance().loadData(null)},1000)


    }


    override fun onError(code: Int, msg: String) {
        showProgress(false)
       if(code == 202) toast("该账号已经注册") else toast(msg)
    }

    override fun onSuccess() {
        showProgress(false)
    }

    override fun getLayoutId(): Int  = R.layout.activity_login



    private fun isUserNameValid(email: String): Boolean {
        return email.length > 1
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 1
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_form.visibility = if (show) View.GONE else View.VISIBLE
            login_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }


}
