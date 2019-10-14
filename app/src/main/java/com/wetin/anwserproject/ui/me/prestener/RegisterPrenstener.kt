package com.wetin.anwserproject.ui.me.prestener

import android.os.Bundle
import android.os.CountDownTimer
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.model.user.remote.UserRemoteModel
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.ui.me.contract.RegisterContract
import retrofit2.Call
import retrofit2.Response

class RegisterPrenstener(var mView:RegisterContract.view):RegisterContract.Prestener {


    override fun sendSms(phone: String) {
        UserRemoteModel.getUserApi().sendSms(phone,"2").enqueue(object :ResultCall<String>{
            override fun onSuccess(call: Call<ResultBean<String>>, response: Response<ResultBean<String>>) {
                mView.showSendSmsSuc()
            }

            override fun onFaile(err: String?) {
                mView.showSendSmsFaile(err)
            }

        })
    }

    override fun register(phone: String, code: String, passs: String) {
        UserRemoteModel.getUserApi().register(phone,passs,code).enqueue(object :ResultCall<String>{
            override fun onSuccess(call: Call<ResultBean<String>>, response: Response<ResultBean<String>>) {
                mView.showRegisterSuc()
            }

            override fun onFaile(err: String?) {
                mView.showRegisterFaile(err)
            }

        })
    }

    /**
     * 做初始化的操作,需要在V的视图初始化完成之后才能调用
     * presenter进行初始化.
     */
    override fun onCreate() {
    }

    /**
     * 容易被回收掉时保存数据
     */
    override fun onSaveInstanceState(outState: Bundle?) {
    }

    /**销毁数据处理*/
    override fun onDestory() {

    }
}