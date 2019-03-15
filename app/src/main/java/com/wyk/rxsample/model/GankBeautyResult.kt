package com.wyk.rxsample.model

import com.google.gson.annotations.SerializedName

data class GankBeautyResult(val error: Boolean, @SerializedName(value = "results")val beauties: List<GankBeauty>)