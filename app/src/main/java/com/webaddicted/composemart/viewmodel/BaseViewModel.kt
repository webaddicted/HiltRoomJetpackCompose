package com.webaddicted.composemart.viewmodel

import androidx.lifecycle.ViewModel
import com.webaddicted.composemart.utils.common.NetworkHelper
import javax.inject.Inject

open class BaseViewModel : ViewModel() {
    @Inject lateinit var  networkHelper: NetworkHelper

}