package com.wyk.rxsample.module.token_advance_5

import android.os.Bundle
import android.support.annotation.StringRes
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.wyk.rxsample.R
import com.wyk.rxsample.base.BaseFragment
import com.wyk.rxsample.model.FakeThing
import com.wyk.rxsample.model.FakeToken
import com.wyk.rxsample.network.Network
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_token_advanced.*

class TokenAdvancedFragment: BaseFragment(), View.OnClickListener {

    var mCacheFakeToken: FakeToken = FakeToken(true)
    var tokenUpdated = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_token_advanced, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        swipeRefreshLayout.isEnabled = false
        requestBt.setOnClickListener(this)
        invalidateTokenBt.setOnClickListener(this)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.requestBt -> {
                val fakeApi = Network.getFakeApi()
                tokenUpdated = true
                unsubscribe()
                swipeRefreshLayout.isRefreshing = true
                mDisposable = Observable.just("fake_auth_code")
                        .flatMap(object: Function<Any,Observable<FakeThing>>{
                            override fun apply(value: Any): Observable<FakeThing> {
                                //流程2
                                //流程5
                                return if(mCacheFakeToken.token == null)
                                            Observable.error<FakeThing>(NullPointerException("Token is Null"))
                                        else
                                            fakeApi.fakeThing(mCacheFakeToken)
                            }
                        })
                         .retryWhen(object: Function<Observable<out Throwable>,Observable<*>>{
                             override fun apply(errorObservable: Observable<out Throwable>): Observable<*> {
                                 //流程1
                                 return errorObservable.flatMap(object: Function<Throwable, Observable<*>>{
                                     override fun apply(throwable: Throwable): Observable<*> {
                                         //流程3
                                        if(throwable is IllegalArgumentException || throwable is NullPointerException) {
                                           //流程4
                                           val tokenObservable = fakeApi.fakeToken("fake_auth_code")
                                                    .doOnNext({
                                                        mCacheFakeToken = it
                                                        tokenUpdated = true
                                                    })
                                            return tokenObservable
                                        }
                                         return Observable.error<Any>(throwable)
                                     }
                                 })

                             }
                         })
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe({
                             //流程6
                             swipeRefreshLayout.isRefreshing = false
                             var token: String = mCacheFakeToken.token
                             if(tokenUpdated){
                                token = "$token(${getString(R.string.updated)})"
                             }
                            tokenTv.text = getString(R.string.got_token_and_data,token, it.id, it.name)
                         },{
                             swipeRefreshLayout.isRefreshing = false
                             showMsg(R.string.loading_failed, Toast.LENGTH_SHORT)
                             it.printStackTrace()
                         })

            }
            R.id.invalidateTokenBt -> {
                mCacheFakeToken.expired = true
                showMsg(R.string.token_expired, Toast.LENGTH_SHORT)
            }
        }
    }

    override fun getTitleRes() = R.string.title_token_advanced
    override fun getDialogRes() = R.layout.dialog_token_advanced

    fun showMsg(@StringRes resId: Int, duration: Int){
        Toast.makeText(activity, resId, duration).show()
    }
    companion object {
        fun  newInstance() = TokenAdvancedFragment()
    }
}