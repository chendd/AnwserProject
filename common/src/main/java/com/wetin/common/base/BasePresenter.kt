package com.wetin.common.base



abstract class BasePresenter<T:IBaseContract.BaseView>(var mView: T?) :IBaseContract.BasePresenter{

     /**
     * 绑定View
     */
    fun onAttch(view: T) {
        this.mView = view

    }


    /**
     * 在V销毁的时候调用,解除绑定
     */
    fun onDetach() {
        this.mView=null
    }


}