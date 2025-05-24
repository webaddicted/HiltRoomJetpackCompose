package com.webaddicted.composemart.data.repo

import com.webaddicted.composemart.data.apiutils.ApiResponse
import com.webaddicted.composemart.data.apiutils.FlowDataFetchCall
import com.webaddicted.composemart.data.model.EcomModel
import com.webaddicted.composemart.data.model.character.CharacterRespo
import com.webaddicted.composemart.utils.common.AppApplication
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor() : BaseRepository() {
    fun getCharacterList(): Flow<ApiResponse<CharacterRespo>> {
        return object : FlowDataFetchCall<CharacterRespo>() {
            override fun createCallAsync(): Deferred<Response<CharacterRespo>> {
                return apiServices.getCharacterList()
            }

            override fun shouldFetchFromDB(): Boolean {
                return false
            }

            override fun internetConnection(): Boolean {
                return networkHelper.isNetworkConnected()
            }
        }.execute()
    }

    fun getEcomData(): Flow<ApiResponse<EcomModel>> = flow {
        try {
            emit(ApiResponse.loading())
            val jsonString = AppApplication.context?.assets?.open("database.json")?.bufferedReader().use { it?.readText() }
            val ecomData = com.google.gson.Gson().fromJson(jsonString, EcomModel::class.java)
            emit(ApiResponse.success(ecomData))
        } catch (e: Exception) {
            emit(ApiResponse.error(e))
        }
    }
}