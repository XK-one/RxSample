package com.wyk.rxsample.module.cache_6.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wyk.rxsample.base.App
import com.wyk.rxsample.model.Item
import java.io.*

const val DATA_FILE_NAME = "data.db"
class DataBase {

    val mDataFile: File = File(App.getInstance().filesDir, DATA_FILE_NAME)
    val mGson: Gson = Gson()

    companion object {
        private var mInstance: DataBase? = null
        fun getInstance() =
                mInstance?: synchronized(this){
                    mInstance?: DataBase().also { mInstance = it }
                }
    }


    fun readItem(): List<Item>?{
        var reader: FileReader? = null
        try{
            Thread.sleep(500)
            reader = FileReader(mDataFile)
            return mGson.fromJson<List<Item>>(reader,object: TypeToken<List<Item>>(){}.type)
        }catch(e: InterruptedException){
            e.printStackTrace()
            return null
        }catch(e: FileNotFoundException){
            e.printStackTrace()
            return null
        }finally {
            reader?.close()
        }
    }

    fun writeItem(items: List<Item>){
        var writer: FileWriter? = null
        try {
            val jsonStr = mGson.toJson(items)
            try {
                if (!mDataFile.exists()) {
                    mDataFile.createNewFile()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            writer = FileWriter(mDataFile)
            writer.write(jsonStr)
            writer.flush()
        }catch (e: IOException){
            e.printStackTrace()
        }finally {
            writer?.close()
        }
    }

    fun deleteFile(){
        mDataFile.delete()
    }


}