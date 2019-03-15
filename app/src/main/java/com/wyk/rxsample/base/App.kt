package com.wyk.rxsample.base

import android.annotation.SuppressLint
import android.app.Application

class App: Application(){
    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mInstance: Application
        fun getInstance() = mInstance
    }
}