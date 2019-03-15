package com.wyk.rxsample.module.token_4

import android.os.Bundle
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.wyk.rxsample.R
import com.wyk.rxsample.base.BaseFragment
import com.wyk.rxsample.model.FakeThing
import com.wyk.rxsample.model.FakeToken
import com.wyk.rxsample.network.Network
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_token.*

class TokenFragment: BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_token, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        swipeRefreshLayout.isEnabled = false
        requestBt.setOnClickListener {
            unsubscribe()
            swipeRefreshLayout.isRefreshing = true
            mDisposable = Network.getFakeApi()
                    .fakeToken("fake_auth_code")
                    //这里可用flatMap，也可用map
                    .map(object: Function<FakeToken,FakeThing>{
                        override fun apply(t: FakeToken): FakeThing {
                            return FakeThing((System.currentTimeMillis() % 1000).toInt(),"FAKE_USER_${System.currentTimeMillis() % 1000}")
                        }
                    })
                    /*.flatMap {
                        Network.getFakeApi().fakeThing(it)
                    }*/
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        swipeRefreshLayout.isRefreshing = false
                        tokenTv.text = getString(R.string.got_data, it.id, it.name)
                    },{
                        swipeRefreshLayout.isRefreshing = false
                        showMsg(R.string.loading_failed, Toast.LENGTH_SHORT)
                        it.printStackTrace()
                    })
        }

    }

    override fun getTitleRes() = R.string.title_token
    override fun getDialogRes() = R.layout.dialog_token

    fun showMsg(@StringRes resId: Int, duration: Int){
        Toast.makeText(activity, resId, duration).show()
    }
    companion object {
        fun newInstance() = TokenFragment()
    }
}