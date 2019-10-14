package com.wetin.anwserproject.net.help

import com.wetin.anwserproject.bean.ResultBean
import retrofit2.Call
import retrofit2.Callback


interface  ResultCall<T>: Callback<ResultBean<T>> {
    override fun onResponse(call: Call<ResultBean<T>>, response: retrofit2.Response<ResultBean<T>>) {
        if (response.isSuccessful){//服务器返回成功
            when (response.body()?.resp_code){
                "000000"->{
                    onSuccess(call,response)
                }
                else->{
                    onFaile(response?.body()?.resp_message)
                }
            }
        }else{
            onFaile("服务器出错")
        }
    }

    override fun onFailure(call: Call<ResultBean<T>>, t: Throwable) {
        onFaile(t.message)
    }
    fun onSuccess(call: Call<ResultBean<T>>, response: retrofit2.Response<ResultBean<T>>)

    fun onFaile(err:String?)
}