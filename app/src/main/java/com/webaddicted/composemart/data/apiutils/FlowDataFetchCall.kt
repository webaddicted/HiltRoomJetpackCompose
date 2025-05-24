package com.webaddicted.composemart.data.apiutils

import com.webaddicted.composemart.utils.common.GlobalUtils
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

abstract class FlowDataFetchCall<ResultType> {
    abstract fun createCallAsync(): Deferred<Response<ResultType>>
    abstract fun shouldFetchFromDB(): Boolean
    abstract fun internetConnection(): Boolean

    fun execute(): Flow<ApiResponse<ResultType>> = flow {
        if (shouldFetchFromDB()) {
            loadFromDB()
        } else {
            try {
                if (internetConnection()) {
                    emit(ApiResponse.loading())
                    val request = createCallAsync()
                    val response = request.await()
                    if (response.isSuccessful && response.body() != null) {
                        saveResult(response.body()!!)
                        emit(ApiResponse.success(response.body()!!))
                    } else {
                        emit(ApiResponse.error(Exception(response.errorBody()?.string())))
                    }
                } else {
                    emit(ApiResponse.internetError())
                }
            } catch (exception: Exception) {
                GlobalUtils.logPrint(msg = exception.toString())
                emit(ApiResponse.error(exception))
            }
        }
    }

    open fun loadFromDB() {}
    open fun saveResult(result: ResultType) {}
}