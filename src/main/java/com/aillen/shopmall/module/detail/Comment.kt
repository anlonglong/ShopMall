package com.aillen.shopmall.module.detail

import android.os.Parcel
import android.os.Parcelable
import cn.bmob.v3.BmobObject


/**
 * Created by anlonglong on 2018/5/7.
 * Email： 940752944@qq.com
 */

data class Comment(var content:String,
                   var username:String,
                   var img_urls:List<String>?,
                   var S_OID:String,
                   var reply:String?):BmobObject(),Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.createStringArrayList(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(content)
        dest.writeString(username)
        dest.writeStringList(img_urls)
        dest.writeString(S_OID)
        dest.writeString(reply)
    }

    override fun describeContents(): Int {
      return 0
    }

    companion object CREATOR : Parcelable.Creator<Comment> {
        override fun createFromParcel(parcel: Parcel): Comment {
            return Comment(parcel)
        }

        override fun newArray(size: Int): Array<Comment?> {
            return arrayOfNulls(size)
        }
    }
}