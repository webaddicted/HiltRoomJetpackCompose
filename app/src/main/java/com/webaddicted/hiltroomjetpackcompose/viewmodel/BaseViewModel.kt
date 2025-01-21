package com.webaddicted.hiltroomjetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import com.webaddicted.hiltroomjetpackcompose.utils.common.NetworkHelper
import javax.inject.Inject

open class BaseViewModel : ViewModel() {
    @Inject lateinit var  networkHelper: NetworkHelper

}