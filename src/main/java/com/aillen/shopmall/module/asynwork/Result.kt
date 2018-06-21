package com.aillen.shopmall.module.asynwork

/**
 * Created by anlonglong on 2018/5/31.
 * Email： 940752944@qq.com
 */


//异步任务的结果处理类，包括监听异步结果，切换结果所在的线程,泛型是返回值的类型
class Result<R> {
    var mResult: R? = null
    var mSuccess: Boolean = false
    var mThreadHandler:ThreadHolder = ThreadHolder.MAIN
    set(value) {
        if (value !=ThreadHolder.ERROR )
            field = value
    }
    private var mFailCount = 0
    private val mListeners:MutableList<AsynResultListener<R>> by lazy { mutableListOf<AsynResultListener<R>>() }
    private lateinit var mException: Exception

    fun addAsynResultListener(listener: AsynResultListener<R>) { mListeners += listener}

    fun removeAsynResultLisenter(listener: AsynResultListener<R>) {mListeners -= listener}

    fun setSuccess(result: R) {
        if (mSuccess) return
        mResult = result
        mSuccess = true
        notifyResultListener()
    }

    fun setError(exception: Exception) {
        ++mFailCount
        mException = exception
        mSuccess = false
        notifyResultListener()
    }

    private fun notifyResultListener() = mListeners.forEach {
        it.onResult(this)
    }
}