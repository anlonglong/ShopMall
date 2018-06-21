package com.aillen.shopmall.base

import android.app.Application
import android.os.Handler
import android.os.Looper
import cn.bmob.v3.Bmob
import com.aillen.shopmall.R
import com.iflytek.cloud.SpeechUtility
import kotlin.properties.Delegates

/**
 * Created by anlonglong on 2018/4/25.
 * Email： 940752944@qq.com
 */
class BaseApplication : Application() {

    companion object {

        private var mApp: BaseApplication by Delegates.notNull()
        private var mHandler: Handler? = null

        fun setApplication(app: BaseApplication) {
            this.mApp = app
        }

        fun getApplication() = mApp

        fun getApplicationHandler(): Handler {
            return if (null != mHandler) mHandler!! else Handler(Looper.getMainLooper())
        }
    }



    override fun onCreate() {
        super.onCreate()
        SpeechUtility.createUtility(this,getString(R.string.speech_appid))
        Bmob.initialize(this,getString(R.string.bmob_appid))
        mApp = this
        BaseApplication.setApplication(this)
    }

}