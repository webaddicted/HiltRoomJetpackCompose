package com.webaddicted.hiltroomjetpackcompose.utils.common


import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for our application.
 */
@HiltAndroidApp
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        com.webaddicted.hiltroomjetpackcompose.utils.sp.PreferenceUtils.Companion.getInstance(this)
//        if(BuildConfig.DEBUG)
//        Stetho.initializeWithDefaults(this)
    }
}