package com.example.hiltroomjetpackcompose.utils.common

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import java.lang.reflect.Type
import kotlin.let

object GlobalUtils {
    //  var restClient: RestClient? = null
    var toast: Toast? = null
    fun Context.showToast(message: String?) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast?.show()
    }

    fun deserializeObj(jsonStr: String, type: Type): Any {
        return Gson().fromJson(jsonStr, type)
    }

    fun serializeObj(obj: Any): String {
        return Gson().toJson(obj)
    }

    fun logPrint(tag: String? = "TAG", msg: String? = "") {
        msg?.let { Log.d(tag, it) }
    }
}