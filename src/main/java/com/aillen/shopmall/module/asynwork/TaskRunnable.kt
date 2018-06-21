package com.aillen.shopmall.module.asynwork

import android.os.Looper
import android.os.Message

/**
 * Created by anlonglong on 2018/5/31.
 * Email： 940752944@qq.com
 */
abstract class TaskRunnable<T>(result: Result<T>) : Runnable {

    private var mResult: Result<T> = result
    private lateinit var  mThreadHandler:ThreadHandler<T>

    override fun run() {
        if (mResult.mThreadHandler == ThreadHolder.MAIN) {
            sendMainMsg()
        }else{
            doSubThreadWork()
        }
    }

    private fun doSubThreadWork() = try {
        val t: T = doWork()
        mResult.setSuccess(t)
    } catch (e: Exception) {
        println("error msg = ${e.message}")
        mResult.setError(e)
    }

    private fun sendMainMsg() {
        mThreadHandler = ThreadHandler(mResult, Looper.getMainLooper())
        val message: Message = mThreadHandler.obtainMessage()
        mThreadHandler.sendMessage(
                try {
                    val t: T = doWork()
                    message.what = mResult.mThreadHandler.code
                    message.obj = t
                    message
                } catch (e: Exception) {
                    message.what = ThreadHolder.ERROR.code
                    message.obj = e
                    message
                }
        )
    }

    abstract fun doWork(): T
}