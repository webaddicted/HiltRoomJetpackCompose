package com.example.hiltroomjetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hiltroomjetpackcompose.utils.common.NetworkHelper
import javax.inject.Inject

open class BaseViewModel : ViewModel() {
    @Inject lateinit var  networkHelper: NetworkHelper

}