package com.example.ahoyandroidtaskmudassar

import android.app.Activity
import android.app.Application
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : MultiDexApplication() {

    companion object {
        private var cInstance: MyApplication? = null
        private var mCurrentActivity: Activity? = null

        @Synchronized
        fun getInstance(): MyApplication {
            return cInstance!!
        }

        fun getCurrentActivity(): Activity? {
            return mCurrentActivity
        }

        fun setCurrentActivity(mCurrentActivity: Activity?) {
            this.mCurrentActivity = mCurrentActivity
        }
    }

    init {
        cInstance = this
    }
}