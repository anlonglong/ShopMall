package com.aillen.shopmall.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.aillen.shopmall.base.BaseFragment.*
import org.greenrobot.eventbus.EventBus
import java.lang.ref.WeakReference

/**
 * Created by anlonglong on 2018/4/26.
 * Email： 940752944@qq.com
 */
open abstract class BaseFragment : Fragment() {


     val ctx: Context
     get() = this.activity?.applicationContext ?: BaseApplication.getApplication() as Context

    private var mRootView:View? = null

    private var hasLoadData = false
    private var isViewPrepared = false

    val mHandler: Handler by lazy { MyHandler(this) }

     class MyHandler(baseFragment: BaseFragment) :Handler(){

        private   var  weakReference : WeakReference<BaseFragment> = WeakReference(baseFragment)

        override fun handleMessage(msg: Message) {
            weakReference.get()?.handleMessage(msg)?:throw NullPointerException()
        }
    }


    fun sendEmptyMessage(what: Int) {
        mHandler.sendEmptyMessage(what)
    }

    fun sendEmptyMessageDelay(what: Int,delayTime: Long){
        mHandler.sendEmptyMessageDelayed(what,delayTime)
    }

    fun postRunnable(r:() ->Unit){
        mHandler.post(r)
    }

    protected open fun handleMessage(msg: Message) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (null != arguments) {
            onFragmentArguement(arguments)
        }
    }

    protected open fun onFragmentArguement(arguments: Bundle) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (null != mRootView) {
            (mRootView?.parent as? ViewGroup)?.removeView(mRootView)
        }else{
            mRootView = inflater.inflate(getLayoutId(),null)
        }
        onVisible()
        return mRootView
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        isViewPrepared = true
        onVisible()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) onVisible() else onInVisible()
    }

    private fun onInVisible() {
        println(this::class.java.simpleName +"不可见")
        Log.d("Fragment",this::class.java.simpleName +"不可见")
    }

    private fun onVisible() {
        if (userVisibleHint && !hasLoadData && isViewPrepared) {
            println(this::class.java.simpleName +"可见")
            Log.d("Fragment",this::class.java.simpleName +"可见")
            if (enableEventBus()) {
                EventBus.getDefault().register(this)
            }
            initView()
            loadData(mRootView)
            intListener()
            hasLoadData = true
        }
    }

    open fun initView() {

    }

     open fun intListener() {

    }

    abstract fun loadData(mRootView: View?)

    protected open fun enableEventBus() = false

    abstract fun getLayoutId(): Int

    override fun onDestroy() {
        if(enableEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        resetFlagState()
        super.onDestroy()
    }

    fun resetFlagState(){
        hasLoadData = false
        isViewPrepared = false
    }

    fun toast(msg:String) {
        Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show()
    }

}