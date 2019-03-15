package com.wyk.rxsample.module.cache_6.data

import android.support.annotation.IntDef
import com.wyk.rxsample.R
import com.wyk.rxsample.base.App
import com.wyk.rxsample.model.Item
import com.wyk.rxsample.network.Network
import com.wyk.rxsample.util.GankBeautyResultToItemsMapper
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

const val DATA_SOURCE_MEMORY = 0
const val DATA_SOURCE_DISK = 1
const val DATA_SOURCE_NETWORK = 2
class Data {
    companion object {
        private var mInstance: Data? = null
        fun getInstance() =
                mInstance?: synchronized(this){
                    mInstance?: Data().also { mInstance = it}
                }
    }

    @IntDef(DATA_SOURCE_MEMORY, DATA_SOURCE_DISK, DATA_SOURCE_NETWORK)
    internal annotation class DataSource

    var mCache: BehaviorSubject<List<Item>>? = null

    var mDataSource: Int? = null
    fun setDataSource(@DataSource dataSource: Int){
        mDataSource = dataSource
    }
    fun getDataSourceText(): String{
        var dataSourceTextRes: Int = R.string.data_source_network
        when(mDataSource){
            DATA_SOURCE_MEMORY ->{
                dataSourceTextRes = R.string.data_source_memory
            }
            DATA_SOURCE_DISK -> {
                dataSourceTextRes = R.string.data_source_disk
            }
            DATA_SOURCE_NETWORK -> {
                dataSourceTextRes = R.string.data_source_network
            }
        }
        return App.getInstance().getString(dataSourceTextRes)
    }

    fun subscribeData(@NonNull onNext: Consumer<List<Item>>, @NonNull onError: Consumer<Throwable>): Disposable {
        if(mCache == null){
            mCache = BehaviorSubject.create()
            //这下面的代码，mCache(BehaviorSubject)是作为观察者
            Observable.create(object: ObservableOnSubscribe<List<Item>>{
                override fun subscribe(e: ObservableEmitter<List<Item>>?) {
                    var diskData: List<Item>? = DataBase.getInstance().readItem()
                    if(diskData== null){
                        loadNetwork()
                        setDataSource(DATA_SOURCE_NETWORK)
                    }else{
                        e?.onNext(diskData)
                        setDataSource(DATA_SOURCE_DISK)
                    }
                }
            })
                    .subscribeOn(Schedulers.io())
                    .subscribe(mCache)//Observable.subscribe(BehaviorSubject)，网络数据或者disk上的数据会被缓存

        }else{
            setDataSource(DATA_SOURCE_MEMORY)
        }
        //这下面的代码，mCache(BehaviorSubject)是作为被观察者
        return mCache!!.doOnError({
            mCache = null
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onNext, onError)//之前缓存的数据将被送达observer

    }

    private fun loadNetwork() {
        Network.getGankApi()
                .getBeauties(100, 1)
                .map(GankBeautyResultToItemsMapper.getInstance())
                .subscribeOn(Schedulers.io())
                .doOnNext({
                    //write disk
                    DataBase.getInstance().writeItem(it)
                })
                .subscribe({
                    mCache?.onNext(it)
                },{
                    mCache?.onError(it)
                })

    }

    fun clearMemoryAndDiskCacheBt(){
        mCache = null
        DataBase.getInstance().deleteFile()
    }
    fun clearMemoryCacheBt(){
        mCache = null
    }

}