package com.example.hiltroomjetpackcompose.data.repo

import com.example.hiltroomjetpackcompose.data.apiutils.ApiResponse
import com.example.hiltroomjetpackcompose.data.apiutils.FlowDataFetchCall
import com.example.hiltroomjetpackcompose.data.model.character.CharacterRespo
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class HomeRepository @Inject constructor() : BaseRepository() {
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
}