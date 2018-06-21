package com.aillen.shopmall.module.category

/**
 * Created by anlonglong on 2018/6/13.
 * Email： 940752944@qq.com
 */
data class VoiceBean(val ws: List<WSBean>)
data class WSBean(val cw: List<CWBean>)
data class CWBean(val w:String)