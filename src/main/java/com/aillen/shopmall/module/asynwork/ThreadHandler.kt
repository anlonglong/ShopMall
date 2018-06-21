package com.aillen.shopmall.module.asynwork

import android.os.Handler
import android.os.Looper
import android.os.Message
import kotlin.properties.Delegates

/**
 * Created by anlonglong on 2018/5/31.
 * Email： 940752944@qq.com
 */


//用来把子线程中的结果发送到主线程中
class ThreadHandler<T>(looper: Looper?):Handler(looper) {

    private var mResult: Result<T> by Delegates.notNull()

    constructor(result: Result<T>,looper: Looper):this(looper){
        mResult = result
    }
    constructor(result: Result<T>):this(null){
        mResult = result
    }

     override fun handleMessage(msg: Message) {
         @Suppress("UNCHECKED_CAST")
         when(msg.what){
             ThreadHolder.MAIN.code,ThreadHolder.SUBTHREAD.code
             -> mResult.setSuccess(msg.obj as T)
             ThreadHolder.ERROR.code
             -> mResult.setError(msg.obj as Exception)
         }
     }
}

enum class ThreadHolder(val code: kotlin.Int) {
    MAIN(0x01),SUBTHREAD(0x02),ERROR(0x03),DELAY(0X03)
}