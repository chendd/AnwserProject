package com.wetin.anwserproject.net.help

import com.google.gson.Gson
import com.wetin.anwserproject.ProjectApplication
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.model.user.local.UserLocalModel
import com.wetin.anwserproject.ui.me.LoginActivity
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.charset.Charset

class ApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        val token = UserLocalModel.newInstanct().getUserData()?.token?:""
        var original = chain?.request()

        var authorised = original?.newBuilder()!!
            .header("token", token)
            .build()
        val response = chain?.proceed(authorised)!!
        if (response?.code() == 500) {//认证用户
            var responseBody = response.body()
            var source = responseBody?.source()
            source?.request(Long.MAX_VALUE)
            var s = source?.buffer()!!.clone().readString(Charset.forName("UTF-8"))
            try {
                val result = Gson().fromJson(s, ResultBean::class.java)
                if (result.resp_code== "999998" || result.resp_code == "999997" || result.resp_code == "999996") {
                    LoginActivity.launcher(ProjectApplication.getInstance())
                }
            } catch (e: Exception) {
                return response
            }

        }
        return chain?.proceed(authorised)!!
    }
}