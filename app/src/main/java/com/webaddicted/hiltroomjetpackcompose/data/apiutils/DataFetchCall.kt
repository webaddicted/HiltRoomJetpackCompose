package com.webaddicted.hiltroomjetpackcompose.data.apiutils

import androidx.lifecycle.MutableLiveData
import com.webaddicted.hiltroomjetpackcompose.utils.common.GlobalUtils
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response


abstract class DataFetchCall<ResultType>(private val responseLiveData: MutableLiveData<ApiResponse<ResultType>>) {
    abstract fun createCallAsync(): Deferred<Response<ResultType>>
    abstract fun shouldFetchFromDB(): Boolean
    abstract fun internetConnection(): Boolean
    open fun loadFromDB() {

    }

    open fun saveResult(result: ResultType) {
    }

    fun execute() {
        if (shouldFetchFromDB()) {
            loadFromDB()
        } else {
            GlobalScope.launch {
                try {
                    if (internetConnection()) {
                        responseLiveData.postValue(ApiResponse.Companion.loading())
                        val request = createCallAsync()
                        val response = request.await()
                        if (response.body() != null) {
                            saveResult(response.body()!!)
                            responseLiveData.postValue(ApiResponse.Companion.success(response.body()!!))
                        } else {
                            responseLiveData.postValue(
                                ApiResponse.Companion.error(
                                    Exception(
                                        response.errorBody()?.string()
                                    )
                                )
                            )
                        }
                    }else responseLiveData.postValue(ApiResponse.Companion.internetError())
                } catch (exception: Exception) {
                    GlobalUtils.logPrint(msg = exception.toString())
                    responseLiveData.postValue(ApiResponse.Companion.error(exception))
                }
            }
        }
    }

}
