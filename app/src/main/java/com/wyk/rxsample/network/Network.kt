package com.wyk.rxsample.network

import com.wyk.rxsample.network.api.FakeApi
import com.wyk.rxsample.network.api.GankApi
import com.wyk.rxsample.network.api.ZhuangbiApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Network  {
    companion object {

        val mOkHttpClient: OkHttpClient = OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))//http访问日志
                .build()
        val gsonConverterFactory: Converter.Factory = GsonConverterFactory.create()
        val rxJavaCallAdapterFactory: CallAdapter.Factory = RxJava2CallAdapterFactory.create()
        @Volatile
        private var mZhuangbiApi: ZhuangbiApi? = null
        fun getZhuangbiApi(): ZhuangbiApi{
            return mZhuangbiApi?: synchronized(this){
                mZhuangbiApi?:
                    Retrofit.Builder()
                            .baseUrl("http://www.zhuangbi.info/")
                            .addConverterFactory(gsonConverterFactory)
                            .addCallAdapterFactory(rxJavaCallAdapterFactory)
                            .client(mOkHttpClient)
                            .build()
                            .create(ZhuangbiApi::class.java)
                            .also { mZhuangbiApi = it }

            }
        }
        @Volatile
        private var mGankApi: GankApi? = null
        fun getGankApi(): GankApi{
            return mGankApi?: synchronized(this){
                mGankApi?:
                        Retrofit.Builder()
                                .baseUrl("http://gank.io/api/")
                                .addConverterFactory(gsonConverterFactory)
                                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                                .client(mOkHttpClient)
                                .build()
                                .create(GankApi::class.java)
                                .also { mGankApi = it }
            }
        }
        @Volatile
        private var mFakeApi: FakeApi? = null
        fun getFakeApi(): FakeApi{
            return mFakeApi?: synchronized(this){
                mFakeApi?: FakeApi().also { mFakeApi = it }
            }
        }

    }
}