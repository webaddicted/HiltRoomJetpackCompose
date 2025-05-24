package com.webaddicted.composemart.data.apiutils

import com.webaddicted.composemart.data.model.character.CharacterRespo
import com.webaddicted.composemart.data.model.common.CommonRespo
import com.webaddicted.composemart.data.model.home.UserInfoRespo
import com.webaddicted.composemart.utils.constant.ApiConstant
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface ApiServices {

    @GET("services/apexrest/userInfoDMS")
    fun getUserInfo(): Deferred<Response<CommonRespo<UserInfoRespo>>>

    @GET(ApiConstant.CHARACTER_API)
    fun getCharacterList(): Deferred<Response<CharacterRespo>>

}


