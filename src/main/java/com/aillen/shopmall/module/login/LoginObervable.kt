package com.aillen.shopmall.module.login

import java.lang.reflect.Method
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by anlonglong on 2018/6/18.
 * Email： 940752944@qq.com
 */
class LoginObervable {
    var changed = false
    private var obs: Vector<LoginObserver> = Vector()
    private val observerClass:Map<Any,Class<*>> by lazy { hashMapOf<Any,Class<*>>() }
    private val observerMethods:HashMap<Any,Method> by lazy { hashMapOf<Any,Method>() }

    @Synchronized
    fun addObserver(observer: LoginObserver) {
//        if (observer == null)
//            throw NullPointerException()
//        if (!obs.contains(observer)) {
//            obs.addElement(observer)
//        }
        if (!observerMethods.containsKey(observer)) {
            findObserverMethods(observer)
        }
    }

    private fun findObserverMethods(observer:LoginObserver) {
        val observerMethod = observer::class.java.getMethod("update", LoginObervable::class.java, Any::class.java, Boolean::class.java)
        if (null != observerMethod) {
            observerMethods[observer] = observerMethod
        }
    }


    @Synchronized
    fun deleteObserver(o: Observer) {
        //obs.removeElement(o)
        observerMethods.remove(o)
    }

    fun notifyObservers() {
        //notifyObservers(null)
    }

    @Synchronized
    fun notifyObservers(arg: Any?,isLogin:Boolean) {
//        var arrLocal: Array<Any>? = null
//        synchronized(this) {
//            if (!hasChanged())
//                return
//            arrLocal = obs.toTypedArray()
//            clearChanged()
//        }
//        for (i in arrLocal!!.indices.reversed()) {
//            (arrLocal!![i] as LoginObserver).update(this, arg,true)
//        }
        for ((observerObj , observerClass) in observerMethods) {
            observerClass.invoke(observerObj,this,arg,isLogin)
        }

    }

    @Synchronized
    fun deleteObservers() {
        //obs.removeAllElements()
        observerMethods.clear()
    }



    @Synchronized
    protected fun clearChanged() {
        changed = false
    }

    @Synchronized
    fun hasChanged(): Boolean {
        return changed
    }

    @Synchronized
    fun countObservers(): Int {
        //return obs.size
        return observerMethods.size
    }
}