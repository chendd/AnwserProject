package com.wetin.anwserproject.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.wetin.common.base.IBaseContract
import com.wetin.common.base.IView
import com.wetin.common.base.ViewManager

abstract class BaseActivity<T: IBaseContract.BasePresenter> : AppCompatActivity(), IView, IBaseContract.BaseView {
    var mPresenter: T?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSetContentBefore()
        setContentView(layoutInflater.inflate(getContentLayout(),null))
        ViewManager.instance.addActivity(this)
        mPresenter=initPresenter()
        mPresenter?.onCreate()
        initDatas(savedInstanceState)
        initWidget()
        bindEvent()
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mPresenter?.onSaveInstanceState(outState)
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.onDestory()
        ViewManager.instance.finishActivity(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    abstract fun initPresenter():T?
}