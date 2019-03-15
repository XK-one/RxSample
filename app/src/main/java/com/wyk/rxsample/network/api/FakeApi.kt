package com.wyk.rxsample.network.api

import com.wyk.rxsample.model.FakeThing
import com.wyk.rxsample.model.FakeToken
import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function
import java.util.*

class FakeApi {

    private val mRandom: Random = Random()

    fun fakeToken(@NonNull fakeAuth: String): Observable<FakeToken> {
        val observable =  Observable.just(fakeAuth)
                .map(object: Function<String,FakeToken>{
                    override fun apply(t: String): FakeToken {
                       val fakeNetworkTimeCost = mRandom.nextInt(500) + 500
                        try{
                            Thread.sleep(fakeNetworkTimeCost.toLong())
                        }catch (e: InterruptedException){
                            e.printStackTrace()
                        }
                        val fakeToken = FakeToken()
                        fakeToken.token = createToken()
                        return fakeToken
                    }
                })

        return observable
    }

    private fun createToken() = "fake_token_${System.currentTimeMillis() % 10000}"

    fun fakeThing(fakeToken: FakeToken): Observable<FakeThing>{
        return Observable.just(fakeToken)
                .map(object: Function<FakeToken, FakeThing>{
                    override fun apply(fakeToken: FakeToken): FakeThing {
                        val fakeNetworkTimeCost = mRandom.nextInt(500) + 500
                        try {
                            Thread.sleep(fakeNetworkTimeCost.toLong())
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                        if (fakeToken.expired) {
                            throw IllegalArgumentException("token is expired")
                        }
                        val fakeThing = FakeThing()
                        fakeThing.id = (System.currentTimeMillis() % 1000).toInt()
                        fakeThing.name = "FAKE_USER_${fakeThing.id}"
                        return fakeThing
                    }
                })
    }
}