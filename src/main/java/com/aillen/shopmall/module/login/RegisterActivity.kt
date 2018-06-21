package com.aillen.shopmall.module.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.view.View
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.user.User
import com.aillen.shopmall.module.user.UserManager
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus

class RegisterActivity : BaseActivity(), UserContract.UserView {


    override fun onError(code: Int, msg: String) {
      showProgress(false)
        if(code == 202) toast("该账号已经注册") else toast(msg)
    }

    override fun onSuccess() {

    }

    override fun onRegister(user: User) {
        showProgress(false)
        toast("注册成功")
        UserManager.isLogin = true
        EventBus.getDefault().post(user)
        finish()
    }

    companion object {
        fun start(activity: LoginActivity) {
            activity.startActivity(Intent(activity, RegisterActivity::class.java))
            activity.finish()
        }
    }

    override fun initView() {

    }


    override fun initListener() {
        gender.setOnCheckedChangeListener { buttonView, isChecked -> buttonView.text = if (isChecked)  "男" else "女"  }
        email_sign_in_register.setOnClickListener { register(user_name.text.toString(),password.text.toString(),input_password_again.text.toString()) }
    }

    private fun register(userName: String, pwd: String, pwd1: String) {

        user_name.error = null
        password.error = null
        input_password_again.error = null

        if (!isUserNameValid(userName)) userName@{
            user_name.error = "用户名不合法"
            user_name.requestFocus()
            return@userName
        }

        if (!isPasswordValid(pwd)) pwd@{
            password.error = "密码长度不够"
            password.requestFocus()
            return@pwd
        }

        if (!equals(pwd,pwd1)) equals@{
            input_password_again.error = "两次密码不一样"
            input_password_again.requestFocus()
            return@equals
        }


        showProgress(true)
        UserPresenter(this).register(userName,pwd,gender.text.toString())
    }

    private fun isUserNameValid(userName: String): Boolean {
        return !TextUtils.isEmpty(userName) && userName.length >= 4
    }

    private fun isPasswordValid(pwd: String): Boolean {
        return !TextUtils.isEmpty(pwd) && pwd.length > 4
    }

    private fun equals(pwd: String,pwd1: String) =  TextUtils.equals(pwd,pwd1)

    override fun getLayoutId(): Int = R.layout.activity_register

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
