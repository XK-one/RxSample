package com.wyk.rxsample.network.api

import com.wyk.rxsample.model.GankBeautyResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface GankApi {

    @GET("data/福利/{number}/{page}")
    fun getBeauties(@Path("number")number: Int, @Path("page")page: Int): Observable<GankBeautyResult>
}