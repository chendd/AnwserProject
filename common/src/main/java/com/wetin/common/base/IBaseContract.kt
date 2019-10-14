package com.wetin.common.base

import android.os.Bundle

interface IBaseContract {
    interface BaseView{
        /**显示提示*/
        fun showInfo(info:String)
        /**加载中*/
        fun showLoading(info:String)
        /**隐藏加载中*/
        fun dimissLoading()

        /**加载失败*/
        fun showError()
        /**无网络*/
        fun showNotNet()
        /**空页面*/
        fun showEmpty()
        /**内容*/
        fun showContent()
    }

    interface BasePresenter{
        /**
         * 做初始化的操作,需要在V的视图初始化完成之后才能调用
         * presenter进行初始化.
         */
        abstract fun onCreate()

        /**
         * 容易被回收掉时保存数据
         */
        abstract fun onSaveInstanceState(outState: Bundle?)

        /**销毁数据处理*/
        fun onDestory()
    }
}