package com.wyk.rxsample.module.zip_3

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.wyk.rxsample.R
import com.wyk.rxsample.adapter.ItemListAdapter
import com.wyk.rxsample.base.BaseFragment
import com.wyk.rxsample.model.Item
import com.wyk.rxsample.model.ZhuangbiImage
import com.wyk.rxsample.module.map_2.MapFragment
import com.wyk.rxsample.network.Network
import com.wyk.rxsample.util.GankBeautyResultToItemsMapper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_zip.*

const val spanCount = 2
const val key = "装逼"
class ZipFragment: BaseFragment() {

    val mAdapter: ItemListAdapter = ItemListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_zip, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        swipeRefreshLayout.isEnabled = false
        gridRv.layoutManager = GridLayoutManager(activity,spanCount)
        gridRv.adapter = mAdapter
        zipLoadBt.setOnClickListener {
            unsubscribe()
            mDisposable = Observable.zip(Network.getGankApi().getBeauties(200,0).map(GankBeautyResultToItemsMapper.getInstance()),
                    Network.getZhuangbiApi().search(key),
                    object: BiFunction<List<Item>,List<ZhuangbiImage>,List<Item>>{
                        override fun apply(items: List<Item>, images: List<ZhuangbiImage>): List<Item> {
                            //zip操作
                            val itemList = mutableListOf<Item>()
                            for(i in 0 until Math.min(items.size/2 ,images.size)){
                                itemList.add(items[i*2])
                                itemList.add(items[i*2+1])
                                val image = images[i]
                                itemList.add(Item(image.description, image.image_url))
                            }
                            return itemList
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mAdapter.setData(it)
                    },{
                        showMsg(R.string.loading_failed, Toast.LENGTH_SHORT)
                        it.printStackTrace()
                    } )
        }
        super.onActivityCreated(savedInstanceState)
    }


    override fun getTitleRes() = R.string.title_zip

    override fun getDialogRes() = R.layout.dialog_zip

    fun showMsg(@StringRes resId: Int, duration: Int){
        Toast.makeText(activity, resId, duration).show()
    }
    companion object {
        fun newInstance() = ZipFragment()
    }
}