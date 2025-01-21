package com.webaddicted.hiltroomjetpackcompose.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.webaddicted.hiltroomjetpackcompose.utils.constant.ApiConstant

@Entity(tableName = ApiConstant.USER_INFO_TABLE)
data class UserInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val customId: Long = 0L,
    @SerializedName("name")val name: String?,
    @SerializedName("email")val email: String?,
    @SerializedName("mobilePhone")val mobilePhone: String?,
    @SerializedName("address")val address: String?
)