package com.wyk.rxsample.base

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import com.wyk.rxsample.module.elementary_1.ElementaryFragment
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.tip_bt.*

abstract class BaseFragment: Fragment(){

    var mDisposable: Disposable? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        tipBt.setOnClickListener {
            AlertDialog.Builder(activity)
                    .setTitle(getTitleRes())
                    .setView(layoutInflater.inflate(getDialogRes(), null))
                    .show()
        }
        super.onActivityCreated(savedInstanceState)
    }


    override fun onDestroy() {
        super.onDestroy()
        unsubscribe()
    }

    fun unsubscribe() {
        if(mDisposable != null && !mDisposable!!.isDisposed){
            mDisposable!!.dispose()
        }
    }

    abstract fun getTitleRes(): Int
    abstract fun getDialogRes(): Int


}