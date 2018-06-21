package com.aillen.shopmall.module.permission

/**
 * Created by anlonglong on 2018/6/12.
 * Email： 940752944@qq.com
 */
interface IPermission {
    /**
     * 用户授权时候的回调
     * @param requestCode
     * @param permission
     */
    fun hasPermission(requestCode: Int, vararg permission: String)

    /**
     * 用户未授权的时候的回调
     * @param requestCode
     * @param permission
     */
    fun permissionDenied(requestCode: Int, vararg permission: String)

    /**
     * 用户未授权并且点击了不再询问的回调
     * @param requestCode
     * @param permission
     */
    fun onNeverAskAgain(requestCode: Int, vararg permission: String)
}