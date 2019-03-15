package com.wyk.rxsample.module.cache_6.data

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.wyk.rxsample.R
import com.wyk.rxsample.adapter.ItemListAdapter
import com.wyk.rxsample.base.BaseFragment
import com.wyk.rxsample.model.Item
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_cache.*

const val spanCount = 2
class CacheFragment: BaseFragment(), View.OnClickListener {

    val mAdapter: ItemListAdapter = ItemListAdapter()

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.loadBt -> {
                swipeRefreshLayout.isRefreshing = true
                unsubscribe()
                val mStartTime = System.currentTimeMillis()
                mDisposable = Data.getInstance()
                        .subscribeData(object: Consumer<List<Item>>{
                            override fun accept(t: List<Item>) {
                                mAdapter.setData(t)
                                swipeRefreshLayout.isRefreshing = false
                                val loadingTime = System.currentTimeMillis() - mStartTime
                                loadingTimeTv.text = getString(R.string.loading_time_and_source, loadingTime, Data.getInstance().getDataSourceText())
                            }
                        },object: Consumer<Throwable>{
                            override fun accept(t: Throwable) {
                                t.printStackTrace()
                                swipeRefreshLayout.isRefreshing = false
                                showMsg(R.string.loading_failed, Toast.LENGTH_SHORT)
                            }
                        })
            }
            R.id.clearMemoryCacheBt ->{
                Data.getInstance().clearMemoryCacheBt()
                showMsg(R.string.memory_cache_cleared,Toast.LENGTH_SHORT)
            }
            R.id.clearMemoryAndDiskCacheBt -> {
                Data.getInstance().clearMemoryAndDiskCacheBt()
                showMsg(R.string.memory_and_disk_cache_cleared,Toast.LENGTH_SHORT)
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cache, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        cacheRv.layoutManager = GridLayoutManager(activity, spanCount)
        cacheRv.adapter = mAdapter
        swipeRefreshLayout.isEnabled = false
        loadBt.setOnClickListener(this)
        clearMemoryCacheBt.setOnClickListener(this)
        clearMemoryAndDiskCacheBt.setOnClickListener(this)
        super.onActivityCreated(savedInstanceState)
    }

    override fun getTitleRes() = R.string.title_cache

    override fun getDialogRes() = R.layout.dialog_cache

    companion object {
        fun newInstance() = CacheFragment()
    }
    fun showMsg(@StringRes resId: Int, duration: Int){
        Toast.makeText(activity, resId, duration).show()
    }
}