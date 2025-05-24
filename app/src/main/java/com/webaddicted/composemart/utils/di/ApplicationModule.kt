package com.webaddicted.composemart.utils.di

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.webaddicted.composemart.BuildConfig
import com.webaddicted.composemart.data.apiutils.ApiServices
import com.webaddicted.composemart.utils.constant.ApiConstant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun getRetrofit(@ApplicationContext context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient(context))
            .build()
    }

    private fun provideOkHttpClient(context: Context): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.connectTimeout(ApiConstant.API_TIME_OUT, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(ApiConstant.API_TIME_OUT, TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(ApiConstant.API_TIME_OUT, TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
//        TODO print response
        interceptor.level = HttpLoggingInterceptor.Level.BODY
//        okHttpClientBuilder.addNetworkInterceptor(StethoInterceptor())
//        if (BuildConfig.DEBUG)interceptor.level = HttpLoggingInterceptor.Level.BODY
//        if (BuildConfig.DEBUG)okHttpClientBuilder.addNetworkInterceptor(StethoInterceptor())
        okHttpClientBuilder.addInterceptor(interceptor)
        return okHttpClientBuilder.build()
    }


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiServices =
        retrofit.create(ApiServices::class.java)

}