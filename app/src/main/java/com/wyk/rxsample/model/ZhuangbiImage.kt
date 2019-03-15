package com.wyk.rxsample.model

data class ZhuangbiImage(val description: String,val image_url: String){
    override fun toString(): String {
        return "ZhuangbiImage(description='$description', image_url='$image_url')"
    }
}