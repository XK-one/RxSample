package com.wyk.rxsample.util

import com.wyk.rxsample.model.GankBeautyResult
import com.wyk.rxsample.model.Item
import io.reactivex.functions.Function
import java.text.ParseException
import java.text.SimpleDateFormat

class GankBeautyResultToItemsMapper: Function<GankBeautyResult, List<Item>>{

    companion object {
        private var mInstance: GankBeautyResultToItemsMapper? = null
        fun getInstance() =  mInstance?: synchronized(this){
            mInstance?: GankBeautyResultToItemsMapper().also { mInstance = it }
        }
    }

    override fun apply(result: GankBeautyResult): List<Item>{
        val beauties = result.beauties
        val items = mutableListOf<Item>()
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'")
        val outputFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        for(gankBeauty in beauties){
            try {
                val date = inputFormat.parse(gankBeauty.createdAt)
                val item = Item(outputFormat.format(date), gankBeauty.url)
                items.add(item)
            }catch(e: ParseException){
                e.printStackTrace()
                items.add(Item("unknow date", gankBeauty.url))
            }
        }
        return items
    }
}