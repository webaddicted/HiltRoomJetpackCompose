package com.webaddicted.composemart.ui.screen.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webaddicted.composemart.R
import com.webaddicted.composemart.ui.theme.ComposeMartTheme
import com.webaddicted.composemart.ui.theme.appColor
import com.webaddicted.composemart.ui.theme.white

class IntroActivity : BaseActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContent {
            ComposeMartTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    IntroScreen(onClick = {
                        startActivity(Intent(this, MainActivity::class.java))
                    })
                }
            }
        }
    }
}

@Preview
@Composable
fun IntroScreen(onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
         .fillMaxSize()
         .background(Color.White)
         .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(70.dp))
        Image(
            painter = painterResource(id = R.drawable.intro_logo),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = R.string.intro_title),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.intro_sub_title),
            fontSize = 18.sp,
            color = Color.DarkGray,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = { onClick() },
//            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(contentColor = white, backgroundColor = appColor),
            modifier = Modifier
             .padding(horizontal = 32.dp, vertical = 16.dp)
             .fillMaxWidth()
                .background(color = appColor)
             .height(50.dp)
        ) {
            Text("Let's Get Start")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "Already Have an account? Sign in",
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.W500
        )
    }
}