package com.wetin.anwserproject.ui.me.contract

import com.wetin.common.base.IBaseContract

interface RegisterContract {
    interface view:IBaseContract.BaseView{
        fun showSendSmsSuc()

        fun showSendSmsFaile(info:String?)

        fun showRegisterSuc()

        fun showRegisterFaile(err:String?)
    }

    interface Prestener:IBaseContract.BasePresenter{
        fun sendSms(phone:String)

        fun register(phone: String,code:String,passs:String)

    }
}