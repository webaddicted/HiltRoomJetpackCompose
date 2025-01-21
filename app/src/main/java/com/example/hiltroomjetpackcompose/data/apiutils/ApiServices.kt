package com.example.hiltroomjetpackcompose.data.apiutils

import com.example.hiltroomjetpackcompose.data.model.character.CharacterRespo
import com.example.hiltroomjetpackcompose.data.model.common.CommonRespo
import com.example.hiltroomjetpackcompose.data.model.home.UserInfoRespo
import com.example.hiltroomjetpackcompose.utils.constant.ApiConstant
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface ApiServices {

    @GET("services/apexrest/userInfoDMS")
    fun getUserInfo(): Deferred<Response<CommonRespo<UserInfoRespo>>>

    @GET(ApiConstant.CHARACTER_API)
    fun getCharacterList(): Deferred<Response<CharacterRespo>>

}


