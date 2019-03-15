package com.wyk.rxsample.module.elementary_1

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import com.wyk.rxsample.R
import com.wyk.rxsample.adapter.ZhuangbiListAdapter
import com.wyk.rxsample.base.BaseFragment
import com.wyk.rxsample.network.Network
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_elementary.*

class ElementaryFragment: BaseFragment(), CompoundButton.OnCheckedChangeListener {

    var mAdapter: ZhuangbiListAdapter = ZhuangbiListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_elementary, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        gridRv?.layoutManager = GridLayoutManager(activity, 2)
        gridRv?.adapter = mAdapter
        swipeRefreshLayout.isEnabled = false
        searchRb1?.setOnCheckedChangeListener(this)
        searchRb2?.setOnCheckedChangeListener(this)
        searchRb3?.setOnCheckedChangeListener(this)
        searchRb4?.setOnCheckedChangeListener(this)

        super.onActivityCreated(savedInstanceState)
    }


    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if(isChecked){
            unsubscribe()
            mAdapter.setData(null)
            serachData(buttonView.text.toString())
        }
    }
    private fun serachData(key: String) {
        mDisposable = Network.getZhuangbiApi()
                .search(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mAdapter.setData(it)

                },{
                    showMsg(R.string.loading_failed, Toast.LENGTH_SHORT)
                    it.printStackTrace()
                })

    }

    override fun getTitleRes() = R.string.title_elementary

    override fun getDialogRes() =  R.layout.dialog_elementary

    companion object {
        fun newInstance() = ElementaryFragment()
    }

    fun showMsg(@StringRes resId: Int,duration: Int){
        Toast.makeText(activity, resId, duration).show()
    }
}
