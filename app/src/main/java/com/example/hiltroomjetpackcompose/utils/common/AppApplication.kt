package com.example.hiltroomjetpackcompose.utils.common


import android.app.Application
import com.example.hiltroomjetpackcompose.utils.sp.PreferenceUtils
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for our application.
 */
@HiltAndroidApp
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        com.example.hiltroomjetpackcompose.utils.sp.PreferenceUtils.Companion.getInstance(this)
//        if(BuildConfig.DEBUG)
//        Stetho.initializeWithDefaults(this)
    }
}