package com.wetin.common.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment<T:IBaseContract.BasePresenter> : Fragment(),IView, IBaseContract.BaseView  {
    var mPresenter: T?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getContentLayout(), container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter=initPresenter()
        mPresenter?.onCreate()
        initDatas(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWidget()
        bindEvent()
    }

    /**显示提示*/
    override fun showInfo(info: String) {

    }

    /**加载中*/
    override fun showLoading(info: String) {
    }

    /**加载失败*/
    override fun showError() {
    }

    /**无网络*/
    override fun showNotNet() {
    }

    /**空页面*/
    override fun showEmpty() {
    }

    /**内容*/
    override fun showContent() {
    }

    override fun dimissLoading() {
    }
    abstract fun initPresenter():T?
}