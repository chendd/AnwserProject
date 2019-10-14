package com.wetin.common.base

import android.os.Bundle

interface IView {
    /**setContent 之前处理*/
    fun initSetContentBefore()
    /**布局*/
     fun getContentLayout():Int
    /**初始化数据*/
    fun initDatas(savedInstanceState: Bundle?)
    /**初始化组件*/
     fun initWidget()
    /**绑定事件*/
     fun bindEvent()
}