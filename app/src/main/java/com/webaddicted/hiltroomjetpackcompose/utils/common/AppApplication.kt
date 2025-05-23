package com.webaddicted.hiltroomjetpackcompose.utils.common


import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for our application.
 */
@HiltAndroidApp
class AppApplication : Application() {
    companion object{
        var context: Context?=null
    }
    override fun onCreate() {
        super.onCreate()
        context = this
        com.webaddicted.hiltroomjetpackcompose.utils.sp.PreferenceUtils.Companion.getInstance(this)
//        if(BuildConfig.DEBUG)
//        Stetho.initializeWithDefaults(this)
    }
}