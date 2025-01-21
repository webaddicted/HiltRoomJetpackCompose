package com.example.hiltroomjetpackcompose.data.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.hiltroomjetpackcompose.data.db.DBConverter
import com.example.hiltroomjetpackcompose.data.db.UserInfoEntity
import com.example.hiltroomjetpackcompose.data.model.home.UserInfoRespo
import com.example.hiltroomjetpackcompose.data.apiutils.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class LoginRepository @Inject constructor() : BaseRepository() {

    fun getDbUserInfoApi(
        emailId: String,
        dbUserInfoResult: MutableLiveData<ApiResponse<UserInfoRespo>>
    ) {
        CoroutineScope(Dispatchers.IO).async {
            val result = userInfoDao.getUserInfoList(emailId)
            dbUserInfoResult.postValue(
                ApiResponse.success(
                    DBConverter.userInfoUiTypeRespo(
                        mutableListOf(
                            result
                        ) as ArrayList<UserInfoEntity>
                    )[0]
                )
            )
        }
    }

    fun setDbUserInfoApi(
        userInfo: UserInfoRespo,
        dbUserInfoRespo: MutableLiveData<ApiResponse<String>>
    ) {
        CoroutineScope(Dispatchers.IO).async {
            try {
                userInfoDao.insertUser(DBConverter.userInfoToDbTypeRespo(mutableListOf(userInfo) as ArrayList<UserInfoRespo>))
                dbUserInfoRespo.postValue(ApiResponse.success(""))
            } catch (e: Exception) {
                Log.e("TAG", "Excep : $e")
                dbUserInfoRespo.postValue(ApiResponse.error(e))
            }

        }
    }
}