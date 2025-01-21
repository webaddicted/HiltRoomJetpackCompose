package com.webaddicted.hiltroomjetpackcompose.data.model.common

import com.google.gson.annotations.SerializedName

data class CommonRespo <T>(
    @SerializedName("isSuccess")
    val isSuccess: Boolean?,
    @SerializedName("respDetails")
    val respDetails: T?,
    @SerializedName("strMessage")
    val strMessage: String?
)