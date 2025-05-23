package com.webaddicted.hiltroomjetpackcompose.ui.screen.activity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.webaddicted.hiltroomjetpackcompose.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
 super.onCreate(savedInstanceState)
 window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
 WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
 window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
 window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
 window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


 }
}