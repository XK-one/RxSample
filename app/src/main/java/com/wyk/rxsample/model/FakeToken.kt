package com.wyk.rxsample.model

data class FakeToken(var token: String, var expired: Boolean){
    constructor(token: String): this(token, false){
    }
    constructor(): this("",false){

    }
    constructor(expired: Boolean): this("",expired){

    }


}