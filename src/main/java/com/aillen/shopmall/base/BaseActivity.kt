package com.aillen.shopmall.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import org.greenrobot.eventbus.EventBus
import java.lang.ref.WeakReference

/**
 * Created by anlonglong on 2018/4/26.
 * Email： 940752944@qq.com
 */
 abstract class BaseActivity : AppCompatActivity() {

    private val mHandler  by lazy { MyHandler(this) }

    protected open fun handlerMessage(msg: Message) {

    }


    class MyHandler(baseActivity: BaseActivity) :Handler(){

        private   var  weakReference : WeakReference<BaseActivity> = WeakReference(baseActivity)

        override fun handleMessage(msg: Message) {
            weakReference.get()?.handlerMessage(msg)?:throw NullPointerException()
        }
    }


    protected fun getHandler() = mHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        if (enableEventBus()) {
            EventBus.getDefault().register(this)
        }
        initView()
        initListener()
        initData()
    }

    fun getIntentExtra(): Intent = this.intent

    protected open fun initData() {

    }

    protected open fun initListener() {

    }

    override fun onDestroy() {
        if (enableEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    fun Context.toast(msg:String) {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }

    fun toast(msg:String) {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }

    protected open fun enableEventBus(): Boolean = false

    abstract fun initView()

    protected fun finishDelay(delayTime:Long){
        getHandler().postDelayed({finish()},delayTime)
    }

    protected fun postRunnable(run:() ->Unit){
        getHandler().post(run)

    }

    protected fun postDelayedTime(r:() ->Unit, uptimeMillins:Long){
        getHandler().postDelayed(r,uptimeMillins)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int


    protected fun displayImage(url:String, imageView: ImageView) {
        Glide.with(this).load(url).into(imageView)
    }

}