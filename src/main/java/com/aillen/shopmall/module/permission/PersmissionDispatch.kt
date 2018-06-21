package com.aillen.shopmall.module.permission

import android.Manifest
import android.app.Activity
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import com.aillen.shopmall.utils.CommonUtils
import pub.devrel.easypermissions.EasyPermissions
import kotlin.properties.Delegates
import kotlin.properties.Delegates.vetoable
import kotlin.reflect.KProperty


/**
 * Created by anlonglong on 2018/6/12.
 * Email： 940752944@qq.com
 */
class PersmissionDispatch():EasyPermissions.PermissionCallbacks {
    private var mActivity: Activity? = null
    private var mFragment: Fragment? = null
    private var mCameraPerm = arrayOf(Manifest.permission.CAMERA)

    //小米手机调用相机并且获取拍照路径的时候，需要两个权限 1.相机权限 2.SD卡权限
    private val mXiaoMiCameraPerm = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val PRODUCT_BRAND = "Xiaomi"

    private var mIPermission: IPermission? = null
    private var mAskPermission = arrayOf<String>()

    constructor(activity: Activity):this() {
        mActivity = activity
    }

    constructor(fragment: Fragment):this(){
        mFragment = fragment
    }


    fun checkPermissionForActivity(requestCode: Int, permission: IPermission, noPermissionDesc: String, vararg perm: String) {
        this.mIPermission = permission
        checkPermissionForActivity(mActivity, requestCode, noPermissionDesc, -1, *perm)
    }

    fun checkPermissionForActivity(requestCode: Int, permission: IPermission, resIdNoPermissionDesc: Int, vararg perm: String) {
        this.mIPermission = permission
        checkPermissionForActivity<Activity>(mActivity, requestCode, null, resIdNoPermissionDesc, *perm)
    }

    fun checkPermissionFragment(requestCode: Int, permission: IPermission, noPermissionDesc: String, vararg perm: String) {
        this.mIPermission = permission
        checkPermissionForFragment<Fragment>(mFragment, requestCode, noPermissionDesc, -1, *perm)
    }


    fun checkPermissionFragment(requestCode: Int, permission: IPermission, resIdNoPermissionDesc: Int, vararg perm: String) {
        this.mIPermission = permission
        checkPermissionForFragment<Fragment>(mFragment, requestCode, null, resIdNoPermissionDesc, *perm)
    }

    private fun <T : Activity> checkPermissionForActivity(t: T?, requestCode: Int, noPermissionDesc: String?, resIdNoPermissionDesc: Int, vararg perm: String) {
        this.mAskPermission = perm as Array<String>
        if (null != t) {
            if (EasyPermissions.hasPermissions(t, *perm)) {
                if (null != mIPermission) {
                    mIPermission!!.hasPermission(requestCode, *perm)
                }
            } else {
                EasyPermissions.requestPermissions(t, noPermissionDesc ?: CommonUtils.getString(mActivity!!, resIdNoPermissionDesc), requestCode, *perm)
            }
        }
    }


    private fun <T : Fragment> checkPermissionForFragment(t: T?, requestCode: Int, noPermissionDesc: String?, resIdNoPermissionDesc: Int, vararg perm: String) {
        this.mAskPermission = perm as Array<String>
        if (null != t) {
            if (EasyPermissions.hasPermissions(t.activity, *perm)) {
                if (null != mIPermission) {
                    this.mIPermission!!.hasPermission(requestCode, *perm)
                }
            } else {
                EasyPermissions.requestPermissions(t, noPermissionDesc ?:CommonUtils.getString(t.activity,resIdNoPermissionDesc),requestCode,*perm)
            }
        }
    }


   override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        //System.out.println("requestCode = [" + requestCode + "], permissions = [" + permissions + "], grantResults = [" + grantResults + "]");
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

   override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        //ToastUtil.showCenterToast(ALApplication.getInstance(),"onPermissionsGranted");

        if (null != mIPermission) {
            mIPermission!!.hasPermission(requestCode, *perms.toTypedArray())
        }

    }

   override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        //ToastUtil.showCenterToast(ALApplication.getInstance(),"onPermissionsDenied");
        //mActivity中的回调
        if (null != mActivity) {
            if (null != mIPermission) {
                if (EasyPermissions.somePermissionDenied(mActivity!!, *perms.toTypedArray())){
                    mIPermission!!.permissionDenied(requestCode, *perms.toTypedArray())
                } else {
                    mIPermission!!.onNeverAskAgain(requestCode, *perms.toTypedArray())
                }
            }
        }


        //mFragment中的回调
        if (null != mFragment) {
            if (null != mIPermission) {
                if (EasyPermissions.somePermissionDenied(mFragment!!, *perms.toTypedArray())) {
                    mIPermission!!.permissionDenied(requestCode, *perms.toTypedArray())
                } else {
                    mIPermission!!.onNeverAskAgain(requestCode, *perms.toTypedArray())
                }
            }
        }
    }
}