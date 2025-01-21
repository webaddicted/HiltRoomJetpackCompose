package com.example.hiltroomjetpackcompose.data.repo

import com.example.hiltroomjetpackcompose.data.db.UserInfoDao
import com.example.hiltroomjetpackcompose.data.model.home.UserInfoRespo
import com.example.hiltroomjetpackcompose.data.apiutils.ApiServices
import com.example.hiltroomjetpackcompose.utils.common.NetworkHelper
import com.example.hiltroomjetpackcompose.utils.sp.PreferenceMgr
import javax.inject.Inject

open class BaseRepository @Inject constructor() {
    @Inject
    lateinit var apiServices: ApiServices

    @Inject
    lateinit var spManager: PreferenceMgr

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var userInfoDao: UserInfoDao

    fun getPrefUserInfo(): UserInfoRespo? {
        return spManager.getUserInfo()
    }

    fun setPrefUserInfo(userModel: UserInfoRespo) {
        spManager.setUserInfo(userModel = userModel)
    }
    fun clearPref() {
        spManager.clearPref()
    }
}
