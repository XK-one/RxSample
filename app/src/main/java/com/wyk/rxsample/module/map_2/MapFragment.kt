package com.wyk.rxsample.module.map_2

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
import com.wyk.rxsample.module.elementary_1.ElementaryFragment
import com.wyk.rxsample.network.Network
import com.wyk.rxsample.util.GankBeautyResultToItemsMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_map.*

const val spanCount = 2
class MapFragment: BaseFragment(), View.OnClickListener {

    var mPage: Int = 0
    var mNum = 10
    val mAdapter: ItemListAdapter = ItemListAdapter()

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.previousPageBt ->{
                loadPage(--mPage)
                if(mPage == 1){
                    previousPageBt.isEnabled = false
                }
            }
            R.id.nextPageBt -> {
                loadPage(++mPage)
                if(mPage == 2){
                    previousPageBt.isEnabled = true
                }
            }
        }
    }

    private fun loadPage(page: Int) {
        unsubscribe()
        mDisposable = Network.getGankApi()
                .getBeauties(mNum, page)
                .map(GankBeautyResultToItemsMapper.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            mAdapter.setData(it)
                            pageTv.text = getString(R.string.page_with_number,this@MapFragment.mPage)
                },{
                    showMsg(R.string.loading_failed, Toast.LENGTH_SHORT)
                    it.printStackTrace()
                })

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        previousPageBt.setOnClickListener(this)
        nextPageBt.setOnClickListener(this)
        swipeRefreshLayout.isEnabled = false
        gridRv.layoutManager = GridLayoutManager(activity, spanCount)
        gridRv.adapter = mAdapter
        super.onActivityCreated(savedInstanceState)
    }


    override fun getTitleRes() = R.string.title_map

    override fun getDialogRes() = R.layout.dialog_map

    fun showMsg(@StringRes resId: Int, duration: Int){
        Toast.makeText(activity, resId, duration).show()
    }

    companion object {
        fun newInstance() = MapFragment()
    }
}