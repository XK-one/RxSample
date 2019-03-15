package com.wyk.rxsample.network.api

import com.wyk.rxsample.model.ZhuangbiImage
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ZhuangbiApi {
    @GET("search")
    fun search(@Query("q")query: String): Observable<List<ZhuangbiImage>>
}