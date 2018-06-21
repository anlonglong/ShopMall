package com.aillen.shopmall.module.asynwork

/**
 * Created by anlonglong on 2018/5/31.
 * Email： 940752944@qq.com
 */
interface AsynResultListener<T> {
    /**
     * 异步任务执行完成后会回调该方法
     */
    fun onResult(result: Result<T>)
}