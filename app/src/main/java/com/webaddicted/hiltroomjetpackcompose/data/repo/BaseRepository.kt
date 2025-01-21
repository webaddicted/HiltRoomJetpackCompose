package com.webaddicted.hiltroomjetpackcompose.data.repo

import com.webaddicted.hiltroomjetpackcompose.data.db.UserInfoDao
import com.webaddicted.hiltroomjetpackcompose.data.model.home.UserInfoRespo
import com.webaddicted.hiltroomjetpackcompose.data.apiutils.ApiServices
import com.webaddicted.hiltroomjetpackcompose.utils.common.NetworkHelper
import com.webaddicted.hiltroomjetpackcompose.utils.sp.PreferenceMgr
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
