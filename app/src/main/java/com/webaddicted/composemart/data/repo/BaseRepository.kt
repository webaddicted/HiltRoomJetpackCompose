package com.webaddicted.composemart.data.repo

import com.webaddicted.composemart.data.db.UserInfoDao
import com.webaddicted.composemart.data.model.home.UserInfoRespo
import com.webaddicted.composemart.data.apiutils.ApiServices
import com.webaddicted.composemart.utils.common.NetworkHelper
import com.webaddicted.composemart.utils.sp.PreferenceMgr
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
